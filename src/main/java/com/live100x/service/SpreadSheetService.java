package com.live100x.service;

import com.live100x.dao.JobPostDao;
import com.live100x.model.JobPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpreadSheetService {
  @Autowired public JobPostDao jobPostDao;

  public List<JobPost> getJobPosts(int startRow, int endRow) {
    return jobPostDao.getJobs(startRow, endRow);
  }
}
