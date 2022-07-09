package com.live100x.dao;

import com.live100x.iterator.LinkedInDBSpreadsheetIterator;
import com.live100x.model.JobPost;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JobPostDao {

  public static final String DATE_POSTED_COLUMN = "Date Posted";
  public static final String ALL_JOB_POST_SHEET_NAME = "all-job-post";

  public List<JobPost> getJobs(int pageNo, int pageSize) {
    List<JobPost> jobPosts = new ArrayList<>();
    LinkedInDBSpreadsheetIterator linkedInDBSpreadsheetIterator =
        new LinkedInDBSpreadsheetIterator(ALL_JOB_POST_SHEET_NAME, pageNo, pageSize);
    while (linkedInDBSpreadsheetIterator.hasNext()) {
      List<Object> row = linkedInDBSpreadsheetIterator.next();
      String datePosted = (String) row.get(0);
      if (!datePosted.equals(DATE_POSTED_COLUMN)) {
        JobPost jobPost =
                new JobPost(
                        (String) row.get(0),
                        (String) row.get(1),
                        (String) row.get(2),
                        (String) row.get(3),
                        (String) row.get(4),
                        (String) row.get(5),
                        (String) row.get(6));
        jobPosts.add(jobPost);
      }
    }
    return jobPosts;
  }
}
