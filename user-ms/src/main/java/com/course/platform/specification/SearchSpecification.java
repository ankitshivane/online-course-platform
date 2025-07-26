package com.course.platform.specification;

import com.course.platform.entity.Role;
import com.course.platform.entity.User;
import com.course.platform.entity.UserActivityLog;
import com.course.platform.model.SearchRequest;
import jakarta.persistence.criteria.*;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.List;

public class SearchSpecification {

    public static void searchByUserName(List<Predicate> predicates, Root<User> root, CriteriaBuilder cb, String name) {
        if(!Strings.isEmpty(name)){
            String likePattern= "%"+name.trim().toLowerCase() +"%";
            predicates.add(cb.like(cb.lower(root.get("name")),likePattern));
        }
    }

    public static void searchByEmail(List<Predicate> predicates, Root<User> root, CriteriaBuilder cb, String email) {
            predicates.add(cb.equal(root.get("email"),email));
    }

//    public static Predicate searchByRefId(List<Predicate> predicates, Root<User> root, CriteriaBuilder cb, String email) {
//        Join<UserActivityLog, User> userUserActivityLogJoin=root.join("user", JoinType.LEFT);
//        predicates.add(cb.equal(root.get("email"),email));
//    }

    public static void searchByRole(List<Predicate> predicates, Root<User> root, CriteriaBuilder cb, SearchRequest request){
        // Join with roles
        if (request.getRoleName() != null) {
            Join<User, Role> roleJoin = root.join("roles", JoinType.INNER);
            predicates.add(cb.equal(cb.lower(roleJoin.get("name")), request.getRoleName().toLowerCase()));
        }


        Join<User, UserActivityLog> logJoin = root.join("activityLogs", JoinType.LEFT);

        if (request.getActivityAction() != null) {
            predicates.add(cb.equal(cb.lower(logJoin.get("action")), request.getActivityAction().toLowerCase()));
        }

        if (request.getPlatform() != null) {
            predicates.add(cb.equal(cb.lower(logJoin.get("platform")), request.getPlatform().toLowerCase()));
        }

        if (request.getStatusCode() != null) {
            predicates.add(cb.equal(logJoin.get("statusCode"), request.getStatusCode()));
        }

        if (request.getReferenceId() != null) {
            predicates.add(cb.equal(logJoin.get("referenceId"), request.getReferenceId().get(0)));
        }

        if (request.getActivityDateFrom() != null && request.getActivityDateTo() != null) {
            predicates.add(cb.between(logJoin.get("timestamp"),
                    request.getActivityDateFrom(), request.getActivityDateTo()));
        }
    }
    public static Predicate referId(List<Long> ranges,Root<User> root, CriteriaBuilder cb ){
        List<String> list = ranges.stream().map(String::valueOf).toList();
        Predicate predicate=null;
        for(String range:list){
            Predicate rangedPredicate=switch (range){
                case "<10"-> cb.lessThan(root.get("referenceId"), new BigDecimal("10"));
                case "1-10m"-> cb.between(root.get("referenceId"),new BigDecimal("1000000"),new BigDecimal("10000000"));
                default -> null;
            };
            if(rangedPredicate!=null){
                predicate=(predicate==null)? rangedPredicate:cb.or(predicate,rangedPredicate);
            }
        }
        return predicate!=null?predicate:cb.conjunction();
    }
}
