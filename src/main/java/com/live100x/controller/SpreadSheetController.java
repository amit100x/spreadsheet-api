package com.live100x.controller;

import com.live100x.dao.JobPostDao;
import com.live100x.model.JobPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpreadSheetController {
  private static final String PAGE = "page", SIZE = "size";

  @Autowired public JobPostDao jobPostDao;

  @GetMapping(
      path = "/getAllJobPost",
      params = {PAGE, SIZE})
  public List<JobPost> getAllJobPost(
      @RequestParam(PAGE) int page,
      @RequestParam(SIZE) int size) {
    return jobPostDao.getJobs(page, size);
  }
}
