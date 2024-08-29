import java.io.*;
import java.util.*;

// Package statement (optional)
// package com.example;

// Custom exception class
class StrongPasswordException extends Exception {
    public StrongPasswordException(String message) {
        super(message);
    }
}

// Abstract class for handling user data
abstract class UserDataHandler {
    private static final String USER_DATA_FILE = "users.txt";
    // Method overloading to handle different types of users
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 2) {
                    users.add(new User(userData[0], userData[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class assignment {
    private static final String EXAM_RESULTS_FILE = "exam_results.txt";
    private static final String USER_DATA_FILE = "users.txt";

    public static void main(String[] args) {
        List<User> users = UserDataHandler.loadUsers(); // Using method from the abstract class
        User user = loginOrRegister(users);
        List<Question> questions = new ArrayList<>();
        Scanner scanner=new Scanner(System.in);

        questions.add(new MultipleChoiceQuestion("What is 2 + 2?", new String[]{"A. 3", "B. 4", "C. 5", "D. 6"}, 2));
        questions.add(new MultipleChoiceQuestion("What is the capital of India?", new String[]{"A. Mumbai", "B. Chennai", "C. Gujarat", "D. Delhi"}, 4));
        questions.add(new MultipleChoiceQuestion("Who is the first Indian cricketer to bag two five-wicket hauls in the World Cup?", new String[]{"A. Jasprit Burah", "B. Mohammed Shami", "C. R Ashwin", "D. Hardik Pandya"}, 2));
        questions.add(new MultipleChoiceQuestion("Who was the first Prime Minister of India?", new String[]{"A. Mahatma Gandhi", "B. Sardar Patel", "C. Jawaharlal Nehru", "D. Subhas Chandra Bose"}, 3));
        questions.add(new MultipleChoiceQuestion("What is the nickname often used to refer to Sardar Patel?", new String[]{"A. Father of the Nation", "B. Iron Man of India", "C. Mahatma", "D. Freedom Fighter"}, 2));
        questions.add(new MultipleChoiceQuestion("Who is the current Prime Minister of India ", new String[]{"A. Narendra Modi", "B. Rahul Gandhi", "C. Manmohan Singh", "D. Arvind Kejriwal"}, 1));
        questions.add(new MultipleChoiceQuestion("Who founded Nirma University?", new String[]{"A. Dr. Karsanbhai Patel", "B. Mukesh Amban", "C. Ratan Tata", "D. Azim Premj"}, 1));
        questions.add(new MultipleChoiceQuestion("Who is the coordinate of OOP course ?", new String[]{"A. Sanjay Patel", "B. Ajay Patel", "C. RT", "D. RRN"}, 2));
        questions.add(new MultipleChoiceQuestion("How many continent in the world", new String[]{"A. 3", "B. 4", "C. 5", "D. 7"}, 4));
        questions.add(new MultipleChoiceQuestion("What is the foundation day of Gujarat?", new String[]{"A. 1/05/1960", "B. 1/05/1965", "C. 1/05/1959", "D. 1/05/1962"},1));

        System.out.print("Enter the number of questions you want to answer: ");
        int numberOfQuestions = scanner.nextInt();
        List<Question> selectedQuestions = selectRandomQuestions(questions, numberOfQuestions);

        MultipleChoiceExam exam = new MultipleChoiceExam(user);
        exam.startExam(selectedQuestions); // Pass the selected questions to the exam
        exam.displayResult();
        
        int totalQuestions;
        totalQuestions = questions.size();

        // Save users after the exam
        UserDataHandler.saveUsers(users); // Using method from the abstract class

        // Save exam results
        saveExamResult(user, exam.getScore(),numberOfQuestions);
    }
    private static List<Question> selectRandomQuestions(List<Question> questions, int numberOfQuestions) {
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(numberOfQuestions, questions.size()));
    }
    private static User loginOrRegister(List<User> users) {
        Scanner scanner = new Scanner(System.in);
        User user = null;
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Please Enter Your choice :");
            System.out.print("1. Register\n2. Login\n ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    user = registerUser(users, scanner);
                    loggedIn = true;
                    break;
                case 2:
                    user = loginUser(users, scanner);
                    if (user != null) {
                        loggedIn = true;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        return user;
    }
    private static User registerUser(List<User> users, Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        int attempts = 2;
        while (attempts > 0) {

                System.out.println("Please Follow the following criteria for the password");
                System.out.println();
                System.out.println("At least 8 characters long");
                System.out.println("Contains at least one uppercase letter");
                System.out.println("Contains at least one lowercase letter");
                System.out.println("Contains at least one digit");
                System.out.println("Contains at least one special character");
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            if (isStrongPassword(password)) {
                User newUser = new User(username, password);
                users.add(newUser);
                System.out.println("Registration successful! ");
                System.out.println("You Can give this Exam: ");

                return newUser;
            } else {
                attempts--;
                System.out.println("At least 8 characters long");
                System.out.println("Contains at least one uppercase letter");
                System.out.println("Contains at least one lowercase letter");
                System.out.println("Contains at least one digit");
                System.out.println("Contains at least one special character");
                if (attempts > 0) {
                    System.out.println("You have " + attempts + " more attempts.");
                } else {
                    System.out.println("You have used all your attempts. Please try to register again the next day.");
                    System.exit(0); // Terminate the program after the maximum attempts are used
                }
            }
        }
        return null;
    }
    private static boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecialChar = true;
            }
        }
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    private static User loginUser(List<User> users, Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                return user;
            }
        }
        System.out.println("Login failed. Invalid username or password.");
        return null;
    }


    private static void saveExamResult(User user, int score, int numberOfQuestions) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXAM_RESULTS_FILE, true))) {
        double percentage = (double) score / numberOfQuestions * 100;
        String formattedPercentage = String.format("%.2f%%", percentage); // Format percentage with two decimal places
        writer.write(user.getUsername() + "," + score + "," + formattedPercentage);
        writer.newLine();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
class Question {
    private String questionText;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }
}
class MultipleChoiceQuestion extends Question {
    private String[] options;
    private int correctAnswer;
    

    public MultipleChoiceQuestion(String questionText, String[] options, int correctAnswer) {
        super(questionText);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
    public String[] getOptions() {
        return options;
    }
    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
interface Exam {
    public void startExam(List<Question> questions);
    public int getScore();
    public void displayResult();
}
class MultipleChoiceExam implements Exam {
    private User user;
    private int score;
    private int totalQuestions;

    public MultipleChoiceExam(User user) {
        this.user = user;
    }

    @Override
    public void startExam(List<Question> questions) {
        this.totalQuestions = questions.size();
        Scanner scanner = new Scanner(System.in);
        int questionNumber = 1; // Initialize the question number
        for (Question question : questions) {
            MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) question; // Casting to MultipleChoiceQuestion
            System.out.println("Question " + questionNumber + ": " + mcQuestion.getQuestionText()); // Display the question number
            for (int i = 0; i < mcQuestion.getOptions().length; i++) {
                System.out.println((i + 1) + ". " + mcQuestion.getOptions()[i]);
            }
            System.out.print("Enter your answer (1-" + mcQuestion.getOptions().length + "): ");
            int userAnswer = scanner.nextInt();

            if (userAnswer == mcQuestion.getCorrectAnswer()) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect.The correct answer is: " + mcQuestion.getOptions()[mcQuestion.getCorrectAnswer() - 1]);
            }
            System.out.println("---------------------------------------------"); // New line after each question
            questionNumber++; // Increment the question number
        }
    }
   public void displayResult() {
    // Add your implementation for the displayResult method here
    System.out.println("Your score: " + score +  " Out of  "+ totalQuestions);
    // Calculate the percentage and print the result accordingly
    // Add your grading logic here as well
    double percentage = (double) score / (totalQuestions)* 100;
    System.out.println("Your Percentage is: " + percentage + "%");

    if (percentage >= 90) {
        System.out.println("Your Grade is: A");
        System.out.println("Congratulations! You are Pass in this exam");
    } else if (percentage >= 80) {
        System.out.println("Your Grade is: B");
        System.out.println("Congratulations! You are Pass in this exam");
    } else if (percentage >= 70) {
        System.out.println("Your Grade is C");
        System.out.println("Congratulations! You are Pass in this exam");
    } else if (percentage >= 60) {
        System.out.println("Your Grade is D");
        System.out.println("Congratulations! You are Pass in this exam");
    } else {
        System.out.println("Your Grade is F");
    System.out.println("Sorry! You have failed in this exam");
    }
}

    public int getScore() {
        return score;
    }
}