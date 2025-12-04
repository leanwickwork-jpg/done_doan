package vn.tuananh.jobhunter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;

import com.turkraft.springfilter.boot.Filter;

import vn.tuananh.jobhunter.domain.Job;
import vn.tuananh.jobhunter.domain.Resume;
import vn.tuananh.jobhunter.domain.Skill;
import vn.tuananh.jobhunter.domain.User;
import vn.tuananh.jobhunter.domain.response.StatisticsDTO;
import vn.tuananh.jobhunter.service.StatisticsService;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<StatisticsDTO> getAllStatistics(@Filter Specification<?> spec) {
        return ResponseEntity.ok(statisticsService.getAllStatistics(spec));
    }

    @GetMapping("/users")
    public ResponseEntity<StatisticsDTO.UserStatistics> getUserStatistics(@Filter Specification<User> spec) {
        return ResponseEntity.ok(statisticsService.getUserStatistics(spec));
    }

    @GetMapping("/jobs")
    public ResponseEntity<StatisticsDTO.JobStatistics> getJobStatistics(@Filter Specification<Job> spec) {
        return ResponseEntity.ok(statisticsService.getJobStatistics(spec));
    }

    @GetMapping("/resumes")
    public ResponseEntity<StatisticsDTO.ResumeStatistics> getResumeStatistics(@Filter Specification<Resume> spec) {
        return ResponseEntity.ok(statisticsService.getResumeStatistics(spec));
    }

    @GetMapping("/skills")
    public ResponseEntity<StatisticsDTO.SkillStatistics> getSkillStatistics(@Filter Specification<Skill> spec) {
        return ResponseEntity.ok(statisticsService.getSkillStatistics(spec));
    }
} 