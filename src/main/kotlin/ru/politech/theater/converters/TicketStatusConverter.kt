package ru.politech.theater.converters

import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter
import ru.politech.theater.model.datastructures.TicketStatus
import java.util.*

class TicketStatusConverter : ConditionalGenericConverter {

  override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair> =
    Collections.singleton(GenericConverter.ConvertiblePair(String::class.java, TicketStatus::class.java))

  override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? =
    TicketStatus.valueOf(source as String)

  override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean =
    targetType.type == TicketStatus::class.java

}
