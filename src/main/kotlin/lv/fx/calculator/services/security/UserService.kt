package lv.fx.calculator.services.security

import lv.fx.calculator.model.security.User
import org.springframework.stereotype.Service


@Service
class UserService {

    val users = listOf(
        User(1,"some@mail.lv","password"),
    )

    fun findByEmail(email: String): User {

        return users.firstOrNull { it.email == email } ?: throw Exception("User not found")
    }

    fun findById(userId: Int): User {

        return users.firstOrNull { it.id == userId } ?: throw Exception("User not found")
    }

}