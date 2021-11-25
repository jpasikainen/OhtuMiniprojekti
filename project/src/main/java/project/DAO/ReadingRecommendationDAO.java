
package project.DAO;

import java.util.ArrayList;

public interface ReadingRecommendationDAO {
    public String getHeadline();
    public void addCourse(String course);
    public ArrayList getRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList getTags();
    public String getPrint();
}
