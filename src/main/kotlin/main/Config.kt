package main

data class Config(val type: String = "Server", val port: Int = 3333, val hostname: String = "localhost", val dir: String = "C:/example") {
    override fun toString(): String {
        return "Type: $type \nPort: $port \nDir: $dir"
    }
}