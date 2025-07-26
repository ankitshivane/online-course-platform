package com.course.platform.service;

import com.course.platform.entity.Role;
import com.course.platform.entity.User;
import com.course.platform.entity.UserActivityLog;
import com.course.platform.model.SearchRequest;
import com.course.platform.model.UserActivityProjection;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserProjectionSearchService {

    @Autowired
    private EntityManager entityManager;

    public Page<UserActivityProjection> searchUserActivity(SearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Main query with multiselect
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<User> user = cq.from(User.class);
        Join<User, Role> roleJoin = user.join("roles", JoinType.LEFT);
        Join<User, UserActivityLog> logJoin = user.join("activityLogs", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getName() != null) {
            predicates.add(cb.like(cb.lower(user.get("name")), "%" + request.getName().toLowerCase() + "%"));
        }

        if (request.getEmail() != null) {
            predicates.add(cb.like(cb.lower(user.get("email")), "%" + request.getEmail().toLowerCase() + "%"));
        }

        if (request.getRoleName() != null) {
            predicates.add(cb.equal(cb.lower(roleJoin.get("name")), request.getRoleName().toLowerCase()));
        }

        if (request.getActivityAction() != null) {
            predicates.add(cb.equal(cb.lower(logJoin.get("action")), request.getActivityAction().toLowerCase()));
        }

        if (request.getPlatform() != null) {
            predicates.add(cb.equal(cb.lower(logJoin.get("platform")), request.getPlatform().toLowerCase()));
        }

        if (request.getActivityDateFrom() != null && request.getActivityDateTo() != null) {
            predicates.add(cb.between(logJoin.get("timestamp"), request.getActivityDateFrom(), request.getActivityDateTo()));
        }

        // Apply predicates and multiselect
        cq.multiselect(
                user.get("name").alias("userName"),
                user.get("email").alias("email"),
                roleJoin.get("name").alias("role"),
                logJoin.get("action").alias("action"),
                logJoin.get("platform").alias("platform"),
                logJoin.get("timestamp").alias("timestamp")
        ).where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);

        // Sort (optional)
        cq.orderBy(cb.asc(user.get("name")));

        // Pagination
        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<UserActivityProjection> resultList = query.getResultList().stream().map(tuple ->
                new UserActivityProjection(
                        tuple.get("userName", String.class),
                        tuple.get("email", String.class),
                        tuple.get("role", String.class),
                        tuple.get("action", String.class),
                        tuple.get("platform", String.class),
                        tuple.get("timestamp", Date.class)
                )).toList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> userRoot = countQuery.from(User.class);
        Join<Object, Object> roleC = userRoot.join("roles", JoinType.LEFT);
        Join<Object, Object> logC = userRoot.join("activityLogs", JoinType.LEFT);
        countQuery.select(cb.countDistinct(userRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }
}
