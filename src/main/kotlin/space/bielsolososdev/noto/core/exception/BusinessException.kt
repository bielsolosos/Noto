package space.bielsolososdev.noto.core.exception

class BusinessException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

