package space.bielsolososdev.noto.domain.users.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true, nullable = false)
    var name: String = "",

    var description: String? = null
)

