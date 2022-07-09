package com.live100x.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class JobPost {
    public String datePosted;
    public String jobTitle;
    public String company;
    public String location;
    public String level;
    public String hrEmailId;
    public String postedBy;
}
