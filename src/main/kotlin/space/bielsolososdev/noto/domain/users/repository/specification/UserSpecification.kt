package space.bielsolososdev.noto.domain.users.repository.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import space.bielsolososdev.noto.domain.users.model.User
import java.time.LocalDateTime

class UserSpecification(
    private val filter: String?,
    private val isActive: Boolean?,
    private val createdAfter: LocalDateTime?,
    private val createdBefore: LocalDateTime?
) : Specification<User> {

    override fun toPredicate(
        root: Root<User>,
        query: CriteriaQuery<*>?,
        cb: CriteriaBuilder
    ): Predicate {
        val predicates = mutableListOf<Predicate>()

        if (!filter.isNullOrBlank()) {
            val searchParams = arrayOf("username", "email")
            val searchPredicates = searchParams.map { param ->
                cb.like(cb.lower(root.get(param)), "%${filter.lowercase().trim()}%")
            }
            predicates.add(cb.or(*searchPredicates.toTypedArray()))
        }

        if (isActive != null) {
            predicates.add(cb.equal(root.get<Boolean>("isActive"), isActive))
        }

        if (createdAfter != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter))
        }

        if (createdBefore != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore))
        }

        return cb.and(*predicates.toTypedArray())
    }
}

