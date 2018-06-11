package io.durbs.scores.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score implements Comparable {

    private String studentId;
    private Integer exam;
    private Double score;

    @Override
    public int compareTo(Object object) {

        val scoreObject = (Score) object;

        return new CompareToBuilder()
            .append(this.getStudentId(), scoreObject.getStudentId())
            .append(this.getExam(), scoreObject.getExam()).build();
    }
}
