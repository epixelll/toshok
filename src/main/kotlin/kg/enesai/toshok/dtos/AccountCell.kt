package kg.enesai.toshok.dtos

import com.opencsv.bean.CsvBindByPosition

class AccountCell {
    @CsvBindByPosition(position = 1)
    var fullname: String? = null
    @CsvBindByPosition(position = 2)
    var address: String? = null
    @CsvBindByPosition(position = 3)
    var checkNumber: String? = null
    @CsvBindByPosition(position = 4)
    var passportNumer: String? = null
    @CsvBindByPosition(position = 6)
    var registeredDate: String? = null
    @CsvBindByPosition(position = 7)
    var sign: String? = null
    @CsvBindByPosition(position = 8)
    var parentName: String? = null
    @CsvBindByPosition(position = 12)
    var phoneNumber: String? = null
}