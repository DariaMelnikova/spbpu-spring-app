package ru.politech.theater

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.format.FormatterRegistry
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.politech.theater.converters.OccupationConverter
import ru.politech.theater.converters.TicketStatusConverter
import ru.politech.theater.model.*
import ru.politech.theater.model.datastructures.*
import ru.politech.theater.services.UserService
import java.security.Principal
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

@SpringBootApplication
@RestController
class TheaterApplication {

  var format = SimpleDateFormat("yyyy-MM-dd")

  @Autowired
  private lateinit var userModel: UserModel

  @Autowired
  private lateinit var performanceModel: PerformanceModel

  @Autowired
  private lateinit var bonusModel: BonusModel

  @Autowired
  private lateinit var ticketModel: TicketModel

  @Autowired
  private lateinit var roleModel: RoleModel

  @RequestMapping(value = ["/roles/decline"], method = [RequestMethod.POST])
  fun refuseRole(@RequestBody role: Role): Boolean {
    roleModel.removeRole(role)
    return true
  }

  @RequestMapping(value = ["/roles/accept"], method = [RequestMethod.POST])
  fun acceptRole(@RequestBody role: Role): Boolean {
    roleModel.acceptRole(role)
    return true
  }

  @RequestMapping("/roles/all")
  fun getRoles(auth: Authentication): List<Role> = roleModel.getAllRolesForActor(userModel.getUser(auth.name))

  data class RoleBody(
    val description: String,
    val actor: User,
    val play: Play
  )

  @RequestMapping(value = ["/bonus/request"], method = [RequestMethod.POST])
  fun requestBonus(@RequestBody bonus: Bonus): Boolean {
    bonusModel.giveBonusTo(userModel.getUser(bonus.actorId), bonus.amount)
    return true
  }

  @RequestMapping("/plays/previous")
  fun getPreviousPlays(auth: Authentication): List<PlayBody> {
    val dir = userModel.getUser(auth.name)
    return performanceModel.allPastPerformances.map { perf ->
      PlayBody(
        Play(perf, -1f, dir, emptyList()),
        userModel.getActorsOnPerformance(perf).distinctBy { it.id }
      )
    }
  }

  @RequestMapping(value = ["/create/role"], method = [RequestMethod.POST])
  fun createRole(auth: Authentication, @RequestBody role: RoleBody): List<PlayBody> {
    roleModel.addRole(role.description, role.play.toPerformance(), role.actor)
    return getUpcomingPlaysForDirector(auth)
  }

  @RequestMapping("/director")
  fun getUpcomingPlaysForDirector(auth: Authentication): List<PlayBody> {
    val dir = userModel.getUser(auth.name)
    return performanceModel.getAllUpcomingPerformancesForDirector(dir).map { perf ->
      PlayBody(
        Play(perf, -1F, dir, roleModel.getAllRolesOnPerformance(perf)),
        userModel.getAvailableActorsForPerformance(perf).distinctBy { user -> user.id }
      )
    }
  }

  data class PlayBody(
    val play: Play,
    val actors: List<User>
  )

  data class UpcomingPerformance(
    val play: Play,
    val tickets: List<TicketBody>
  )

  @RequestMapping(value = ["/tickets/return"], method = [RequestMethod.POST])
  fun requestReturn(@RequestBody ticket: TicketBody): Boolean {
    ticketModel.requestReturn(BoughtTicket(
      ticket.toTicket(),
      ticket.play.toPerformance()
    ))
    return true
  }

  @RequestMapping("/tickets/bought")
  fun getBoughtTickets(auth: Authentication): List<TicketBody> =
    ticketModel.getBoughtTicketsForUser(userModel.getUser(auth.name)).map { ticket ->
      TicketBody(
        ticket.ticket,
        Play(ticket.performance, ticket.ticket.price, userModel.getUser(ticket.performance.directorId))
      )
    }

  @RequestMapping(value = ["/tickets/buy"], method = [RequestMethod.POST])
  fun buyTicket(auth: Authentication, @RequestBody ticket: TicketBody): Boolean {
    ticketModel.buyTicket(ticket.toTicket(), userModel.getUser(auth.name))
    return true
  }

  fun TicketBody.toTicket() = Ticket(
    id.toInt(),
    place,
    price,
    format.parse(date).time,
    play.id.toInt(),
    status
  )

  @RequestMapping("/tickets/upcoming")
  fun getUpcomingPlays(): List<UpcomingPerformance> =
    performanceModel.allUpcomingPerformancesWithAvailableTickets.map { performance ->
      val tickets = ticketModel.getAvailableSitsForPerformance(performance)
      val play = Play(performance, tickets.first().price, userModel.getUser(performance.directorId))
      UpcomingPerformance(
        play,
        tickets.map { ticket -> TicketBody(ticket, play) }
      )
    }

  data class TicketBody(
    val id: String,
    val place: Int,
    val price: Float,
    val date: String,
    val play: Play,
    val status: TicketStatus
  ) {
    constructor(ticket: Ticket, play: Play) : this(
      ticket.id.toString(),
      ticket.place,
      ticket.price,
      SimpleDateFormat("yyyy-MM-dd").format(Date(ticket.dateMilli)),
      play,
      ticket.ticketStatus
    )
  }

  @RequestMapping("/tickets/all")
  fun getPendingTickets(request: HttpServletRequest): List<TicketBody> = ticketModel.pendingBoughtTickets.map { pendingTicket ->
    TicketBody(
      pendingTicket.ticket,
      Play(pendingTicket.performance, pendingTicket.ticket.price, userModel.getUser(pendingTicket.performance.directorId))
    )
  }

  @RequestMapping(value = ["/tickets/approve"], method = [RequestMethod.POST])
  fun approveTicketBuying(@RequestBody boughtTicketBody: TicketBody): Boolean {
    ticketModel.approveBuying(BoughtTicket(
      boughtTicketBody.toTicket(),
      boughtTicketBody.play.toPerformance()
    ))
    return true
  }

  fun Play.toPerformance() = Performance(
    id.toInt(),
    name,
    description,
    format.parse(date).time,
    director.id
  )

  @RequestMapping(value = ["/tickets/refuse"], method = [RequestMethod.POST])
  fun refuseTicketReturn(@RequestBody boughtTicketBody: TicketBody): Boolean {
    ticketModel.refuseReturn(BoughtTicket(
      boughtTicketBody.toTicket(),
      boughtTicketBody.play.toPerformance()
    ))
    return true
  }

  @RequestMapping("/bonus/all")
  fun getAllBonusRequests(request: HttpServletRequest): List<BonusRequest> = bonusModel.all

  @RequestMapping(value = ["/bonus/give"], method = [RequestMethod.POST])
  fun giveBonus(@RequestBody bonusRequest: BonusRequest): Boolean {
    bonusModel.removeBonus(bonusRequest)
    return true
  }

  @RequestMapping(value = ["/bonus/refuse"], method = [RequestMethod.POST])
  fun refuseBonus(@RequestBody bonusRequest: BonusRequest): Boolean {
    bonusModel.removeBonus(bonusRequest)
    return true
  }

  @RequestMapping("/resource")
  fun home(request: HttpServletRequest): List<User> = userModel.getAllUsersWithOccupation(Occupation.VIEWER)

  @RequestMapping("/directors")
  fun directors(request: HttpServletRequest): List<User> = userModel.getAllUsersWithOccupation(Occupation.DIRECTOR)

  @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
  fun createUser(@RequestBody createUserRequest: User): User {
    userModel.addUser(
      createUserRequest.login,
      createUserRequest.firstName,
      createUserRequest.lastName,
      createUserRequest.password,
      createUserRequest.occupation
    )
    return createUserRequest
  }

  data class Play(
    val id: String,
    val name: String,
    val description: String,
    val date: String,
    val ticketPrice: Float,
    val director: User,
    val roles: List<Role> = emptyList()
  ) {
    constructor(perf: Performance, price: Float, director: User, roles: List<Role> = emptyList()) : this(
      perf.id.toString(),
      perf.name,
      perf.description,
      SimpleDateFormat("yyyy-MM-dd").format(Date(perf.dateMilli)),
      price,
      director,
      roles
    )
  }

  @RequestMapping(value = ["/create/play"], method = [RequestMethod.POST])
  fun createPlay(@RequestBody createPlayRequest: Play): Play {
    performanceModel.addPerformance(
      createPlayRequest.name,
      createPlayRequest.description,
      createPlayRequest.director,
      SimpleDateFormat("yyyy-MM-dd").parse(createPlayRequest.date).time,
      createPlayRequest.ticketPrice
    )
    return createPlayRequest
  }

  @RequestMapping("/user")
  fun user(user: Principal): User = userModel.getUser(user.name)

  @Bean
  fun getUserModel() = UserModel()

  @Bean
  fun getPerformaceModel() = PerformanceModel()

  @Bean
  fun getBonusModel() = BonusModel()

  @Bean
  fun getTicketsModel() = TicketModel()

  @Bean
  fun getRoleModel() = RoleModel()

  @Configuration
  class CustomWebMvcConfigurerAdapter : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
      registry.addConverter(OccupationConverter())
      registry.addConverter(TicketStatusConverter())
    }
  }

  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  protected class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userService: UserService

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
      auth.userDetailsService(userService).passwordEncoder(BCryptPasswordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
      http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/index.html", "/home").permitAll()
        .anyRequest().authenticated()
        .and()
        .logout()
        .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .and()
        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    }

  }

}

fun main(args: Array<String>) {
  runApplication<TheaterApplication>(*args)
}
