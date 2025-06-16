package com.astrapay.util;

import com.astrapay.dto.NoteFilterRequest;
import com.astrapay.entity.Note;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class NoteSpecification {

    public static Specification<Note> withTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(title)) {
                return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
                );
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    public static Specification<Note> withContent(String content) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(content)) {
                return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("content")),
                    "%" + content.toLowerCase() + "%"
                );
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

    /**
     * Creates a combined specification based on the filter request
     *
     * @param filter The note filter request containing filter criteria
     * @return A specification combining all filter criteria
     */
    public static Specification<Note> buildSpecification(NoteFilterRequest filter) {
        if (filter == null) {
            return null;
        }

        Specification<Note> spec = Specification.where(null);

        if (StringUtils.hasText(filter.getTitle())) {
            spec = spec.and(withTitle(filter.getTitle()));
        }

        if (StringUtils.hasText(filter.getContent())) {
            spec = spec.and(withContent(filter.getContent()));
        }

        return spec;
    }
}
