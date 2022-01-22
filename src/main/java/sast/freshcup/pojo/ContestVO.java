package sast.freshcup.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sast.freshcup.entity.Contest;

import java.time.LocalDateTime;

/**
 * @author: 風楪fy
 * @create: 2022-01-22 22:17
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestVO {
    private Long id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private Boolean enable;

    public ContestVO(Contest contest) {
        this.id = contest.getId();
        this.name = contest.getName();
        this.start = contest.getStart();
        this.end = contest.getEnd();
        this.description = contest.getDescription();
    }
}
