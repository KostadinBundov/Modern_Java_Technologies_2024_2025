public class CourseScheduler {
    private static void sort(int[][] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minElementIndex = i;

            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j][0] < arr[minElementIndex][0]) {
                    minElementIndex = j;
                }
            }

            if (minElementIndex != i) {
                int[] temp = arr[i];
                arr[i] = arr[minElementIndex];
                arr[minElementIndex] = temp;
            }
        }
    }

    public static int maxNonOverlappingCourses(int[][] courses) {
        sort(courses);

        if(courses.length == 1) {
            return 1;
        }

        if(courses.length == 0) {
            return 0;
        }

        int maxCoursesCount = Integer.MIN_VALUE;

        for (int i = 0; i < courses.length - 1; i++) {
            int lastEndHour = courses[i][1];
            int currentCoursesCount = 1;

            for (int j = i + 1; j < courses.length; j++) {
                if (courses[j][0] >= lastEndHour) {
                    currentCoursesCount++;
                    lastEndHour = courses[j][1];
                }
            }

            if (currentCoursesCount > maxCoursesCount) {
                maxCoursesCount = currentCoursesCount;
            }
        }

        return maxCoursesCount;
    }
}

class Main {
    public static void main(String[] args) {
        int result = CourseScheduler.maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}});
        System.out.println("Maximum number of non-overlapping courses: " + result);
    }
}