package net.skhu.model;

import lombok.Data;

@Data
public class EditProjectModel {
   String projectName;
   int memNum;
   String theme;
   String content;
   String tag;
   String github;
   boolean rcrtState;
   int subject;
}