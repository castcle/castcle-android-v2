package com.castcle.android.core.error

sealed class CastcleException(override val message: String?) : Throwable(message) {

    class ContentNotFoundException(override val message: String?) : CastcleException(message)

    class IncorrectEmail(override val message: String?) : CastcleException(message)

    class IncorrectEmailOrPassword(override val message: String?) : CastcleException(message)

    class SocialMediaAlreadyConnected(override val message: String?) : CastcleException(message)

    class UserNotFoundException(override val message: String?) : CastcleException(message)

}