package vn.tuananh.jobhunter.domain.response.post;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.tuananh.jobhunter.domain.Job;
import vn.tuananh.jobhunter.domain.User;

@Getter
@Setter
public class ResCreatePostDTO {
    private long id;
    private String title;

    private String content;

    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private Job job;
    private User user;

    private Instant createdAt;
    private String createdBy;

}