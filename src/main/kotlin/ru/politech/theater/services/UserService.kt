package ru.politech.theater.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.politech.theater.model.UserModel
import ru.politech.theater.model.repositories.UserRepository

@Service
class UserService : UserDetailsService {

  @Autowired
  private lateinit var userModel: UserModel

  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(login: String): UserDetails {
    val user = userModel.getUser(login) ?: throw UsernameNotFoundException(login)
    val auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.occupation.name)

    return User(
      user.login,
      BCryptPasswordEncoder().encode(user.password),
      true,
      true,
      true,
      true,
      auth
    )
  }

}
