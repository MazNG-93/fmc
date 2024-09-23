import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("interval radius");
        Scanner sc = new Scanner(System.in);

        Radar radar = new Radar(100.0, 1.0); // Bán kính quét radar là 100 đơn vị
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Thêm vật thể");
            System.out.println("2. Quét radar");
            System.out.println("3. Dự đoán vị trí vật thể");
            System.out.println("4. Cập nhật vị trí vật thể");
            System.out.println("5. Xóa vật thể");
            System.out.println("6. Tìm kiếm vật thể theo tên");
            System.out.println("7. Hiển thị lịch sử quét radar");
            System.out.println("8. Sửa thông tin vật thể");
            System.out.println("9. Kiểm tra vật thể ra khỏi phạm vi radar");
            System.out.println("10. Bắt đầu tự động cập nhật vị trí");
            System.out.println("11. Dừng tự động cập nhật vị trí");
            System.out.println("12. Tạo vật thể ngẫu nhiên");
            System.out.println("13. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên vật thể: ");
                    scanner.nextLine();  // Đọc bỏ dòng thừa
                    String name = scanner.nextLine();
                    System.out.print("Nhập tọa độ X: ");
                    double x = scanner.nextDouble();
                    System.out.print("Nhập tọa độ Y: ");
                    double y = scanner.nextDouble();
                    System.out.print("Nhập vận tốc: ");
                    double speed = scanner.nextDouble();
                    System.out.print("Nhập hướng (độ): ");
                    double direction = scanner.nextDouble();
                    ObjectDetected obj = new ObjectDetected(name, x, y, speed, direction);
                    radar.addObject(obj);
                    break;
                case 2:
                    radar.scan();
                    break;
                case 3:
                    System.out.print("Nhập thời gian dự đoán (giây): ");
                    double deltaTime = scanner.nextDouble();
                    radar.predictPositions(deltaTime);
                    break;
                case 4:
                    System.out.print("Nhập tên vật thể cần cập nhật: ");
                    scanner.nextLine();  // Đọc bỏ dòng thừa
                    String updateName = scanner.nextLine();
                    System.out.print("Nhập tọa độ X mới: ");
                    double newX = scanner.nextDouble();
                    System.out.print("Nhập tọa độ Y mới: ");
                    double newY = scanner.nextDouble();
                    System.out.print("Nhập vận tốc mới: ");
                    double newSpeed = scanner.nextDouble();
                    System.out.print("Nhập hướng mới: ");
                    double newDirection = scanner.nextDouble();
                    radar.editObject(updateName, newX, newY, newSpeed, newDirection);
                    break;
                case 5:
                    System.out.print("Nhập tên vật thể cần xóa: ");
                    scanner.nextLine();  // Đọc bỏ dòng thừa
                    String deleteName = scanner.nextLine();
                    radar.removeObject(deleteName);
                    break;
                case 6:
                    System.out.print("Nhập tên vật thể cần tìm: ");
                    scanner.nextLine();  // Đọc bỏ dòng thừa
                    String searchName = scanner.nextLine();
                    radar.searchObjectByName(searchName);
                    break;
                case 7:
                    radar.displayScanHistory();
                    break;
                case 8:
                    System.out.print("Nhập tên vật thể cần sửa: ");
                    scanner.nextLine();  // Đọc bỏ dòng thừa
                    String editName = scanner.nextLine();
                    System.out.print("Nhập tọa độ X mới: ");
                    double editX = scanner.nextDouble();
                    System.out.print("Nhập tọa độ Y mới: ");
                    double editY = scanner.nextDouble();
                    System.out.print("Nhập vận tốc mới: ");
                    double editSpeed = scanner.nextDouble();
                    System.out.print("Nhập hướng mới: ");
                    double editDirection = scanner.nextDouble();
                    radar.editObject(editName, editX, editY, editSpeed, editDirection);
                    break;
                case 9:
                    radar.checkOutOfRangeObjects();
                    break;
                case 10:
                    System.out.print("Nhập khoảng thời gian cập nhật tự động (giây): ");
                    int interval = scanner.nextInt();
                    radar.startAutoUpdate(interval);
                    break;
                case 11:
                    radar.stopAutoUpdate();
                    break;
                case 12:
                    radar.generateRandomObjects(); // Gọi phương thức tạo vật thể ngẫu nhiên
                    break;
                case 13:
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }

        scanner.close();
        System.out.println("Chương trình đã kết thúc.");
    }
}
