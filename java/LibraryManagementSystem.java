import java.io.*;
import java.util.*;

/**
 * Library Management System (Console-based)
 * -----------------------------------------
 * This project demonstrates OOP, Java Collections, and file handling.
 * It allows adding, issuing, returning, viewing, and deleting books.
 *
 * Created for Hacktoberfest 2025 by [Your Name]
 */
public class LibraryManagementSystem {

    // ---------- Inner Model Class ----------
    static class Book implements Serializable {
        private int id;
        private String title;
        private String author;
        private boolean issued;

        public Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.issued = false;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isIssued() {
            return issued;
        }

        public void setIssued(boolean issued) {
            this.issued = issued;
        }

        @Override
        public String toString() {
            return String.format("ID: %-3d | %-25s | %-15s | %s",
                    id, title, author, (issued ? "Issued" : "Available"));
        }
    }

    // ---------- Library Class ----------
    static class Library {
        private List<Book> books;
        private final String DATA_FILE = "library_data.txt";

        public Library() {
            books = loadBooks();
        }

        // Add a book
        public void addBook(Book book) {
            books.add(book);
            saveBooks();
            System.out.println("✅ Book added successfully!");
        }

        // Issue a book
        public void issueBook(int id) {
            for (Book b : books) {
                if (b.getId() == id) {
                    if (!b.isIssued()) {
                        b.setIssued(true);
                        saveBooks();
                        System.out.println("📘 Book issued successfully!");
                        return;
                    } else {
                        System.out.println("⚠️ Book is already issued.");
                        return;
                    }
                }
            }
            System.out.println("❌ Book not found!");
        }

        // Return a book
        public void returnBook(int id) {
            for (Book b : books) {
                if (b.getId() == id) {
                    if (b.isIssued()) {
                        b.setIssued(false);
                        saveBooks();
                        System.out.println("✅ Book returned successfully!");
                        return;
                    } else {
                        System.out.println("⚠️ This book was not issued.");
                        return;
                    }
                }
            }
            System.out.println("❌ Book not found!");
        }

        // Remove a book
        public void removeBook(int id) {
            boolean removed = books.removeIf(b -> b.getId() == id);
            if (removed) {
                saveBooks();
                System.out.println("🗑️ Book removed successfully!");
            } else {
                System.out.println("❌ Book not found!");
            }
        }

        // Display all books
        public void displayBooks() {
            if (books.isEmpty()) {
                System.out.println("📭 No books in the library yet!");
                return;
            }
            System.out.println("\n========= 📚 Library Books =========");
            books.forEach(System.out::println);
            System.out.println("====================================");
        }

        // Save to file
        private void saveBooks() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(books);
            } catch (IOException e) {
                System.err.println("Error saving data: " + e.getMessage());
            }
        }

        // Load from file
        @SuppressWarnings("unchecked")
        private List<Book> loadBooks() {
            File file = new File(DATA_FILE);
            if (!file.exists())
                return new ArrayList<>();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Book>) ois.readObject();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }

    // ---------- Main Menu ----------
    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1️⃣  Add Book");
            System.out.println("2️⃣  Issue Book");
            System.out.println("3️⃣  Return Book");
            System.out.println("4️⃣  View All Books");
            System.out.println("5️⃣  Remove Book");
            System.out.println("6️⃣  Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author Name: ");
                    String author = sc.nextLine();
                    library.addBook(new Book(id, title, author));
                    break;

                case 2:
                    System.out.print("Enter Book ID to issue: ");
                    library.issueBook(sc.nextInt());
                    break;

                case 3:
                    System.out.print("Enter Book ID to return: ");
                    library.returnBook(sc.nextInt());
                    break;

                case 4:
                    library.displayBooks();
                    break;

                case 5:
                    System.out.print("Enter Book ID to remove: ");
                    library.removeBook(sc.nextInt());
                    break;

                case 6:
                    System.out.println("👋 Exiting Library System. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
