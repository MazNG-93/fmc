import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Radar {
    private double radius; // Bán kính quét radar
    private List<ObjectDetected> objects; // Danh sách vật thể trong tầm radar
    private double scanSpeed; // Tốc độ quét radar (vòng/s)
    private Connection connection;
    private Timer timer = new Timer();
    private List<String> scanHistory = new ArrayList<>();

    public Radar(double radius, double scanSpeed) {
        this.radius = radius;
        this.scanSpeed = scanSpeed;
        this.objects = new ArrayList<>();
        connectToDatabase();
    }

    // Kết nối với SQL Server
    private void connectToDatabase() {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=RadarSystem;user=sa;password=12345;trustServerCertificate=true;";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm vật thể vào danh sách radar
    public void addObject(ObjectDetected obj) {
        objects.add(obj);
    }

    // Xóa vật thể khỏi radar
    public void removeObject(String objectName) {
        objects.removeIf(obj -> obj.getName().equalsIgnoreCase(objectName));
        System.out.println("Đã xóa vật thể: " + objectName);
    }

    // Tìm kiếm vật thể theo tên
    public void searchObjectByName(String objectName) {
        for (ObjectDetected obj : objects) {
            if (obj.getName().equalsIgnoreCase(objectName)) {
                System.out.println(obj);
                return;
            }
        }
        System.out.println("Không tìm thấy vật thể có tên: " + objectName);
    }

    // Quét và phát hiện vật thể trong tầm radar
    public void scan() {
        System.out.println("Quét radar...");
        StringBuilder scanLog = new StringBuilder("Lịch sử quét radar: \n");
        boolean objectDetected = false;

        for (ObjectDetected obj : objects) {
            if (obj.getDistance() <= radius) {
                System.out.println(obj);  // Hiển thị thông tin vật thể
                scanLog.append(obj.toString()).append("\n");
                saveToDatabase(obj);
                objectDetected = true;
            }
        }

        if (!objectDetected) {
            System.out.println("Không phát hiện vật thể nào trong tầm quét.");
        } else {
            scanHistory.add(scanLog.toString());
        }
    }

    // Hiển thị lịch sử quét radar
    public void displayScanHistory() {
        if (scanHistory.isEmpty()) {
            System.out.println("Chưa có lịch sử quét radar.");
        } else {
            for (String history : scanHistory) {
                System.out.println(history);
            }
        }
    }

    // Cập nhật vị trí của các vật thể
    public void updateObjects(double deltaTime) {
        for (ObjectDetected obj : objects) {
            obj.updatePosition(deltaTime);
        }
    }

    // Dự đoán vị trí của các vật thể trong tương lai
    public void predictPositions(double deltaTime) {
        System.out.println("Dự đoán vị trí trong " + deltaTime + " giây tới:");
        for (ObjectDetected obj : objects) {
            obj.updatePosition(deltaTime);
            System.out.println(obj);
        }
    }

    // Sửa thông tin vật thể
    public void editObject(String objectName, double newX, double newY, double newSpeed, double newDirection) {
        for (ObjectDetected obj : objects) {
            if (obj.getName().equalsIgnoreCase(objectName)) {
                obj.updatePosition(newX, newY);
                obj.setSpeed(newSpeed);
                obj.setDirection(newDirection);
                System.out.println("Đã cập nhật thông tin cho vật thể: " + objectName);
                return;
            }
        }
        System.out.println("Không tìm thấy vật thể có tên: " + objectName);
    }

    // Kiểm tra vật thể ra khỏi phạm vi radar
    public void checkOutOfRangeObjects() {
        for (ObjectDetected obj : objects) {
            if (obj.getDistance() > radius) {
                System.out.println("Cảnh báo: " + obj.getName() + " đã ra khỏi phạm vi radar!");
            }
        }
    }

    // Tự động cập nhật vị trí
    public void startAutoUpdate(int intervalInSeconds) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateObjects(1.0);  // Cập nhật mỗi giây
                System.out.println("Tự động cập nhật vị trí vật thể.");
            }
        }, 0, intervalInSeconds * 1000);
    }

    // Dừng cập nhật tự động
    public void stopAutoUpdate() {
        timer.cancel();
        System.out.println("Đã dừng cập nhật tự động.");
    }

    // Lưu thông tin vào cơ sở dữ liệu
    private void saveToDatabase(ObjectDetected obj) {
        String query = "EXEC InsertRadarScan ?, ?, ?, ?, ?, ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, obj.getName());
            statement.setDouble(2, obj.getX());
            statement.setDouble(3, obj.getY());
            statement.setDouble(4, obj.getDistance());
            statement.setDouble(5, obj.getSpeed());
            statement.setDouble(6, obj.getDirection());
            statement.executeUpdate();
            System.out.println("Lưu dữ liệu thành công vào SQL Server.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tạo vật thể ngẫu nhiên
    public void generateRandomObjects() {
        Random random = new Random();
        int count = random.nextInt(500) + 1; // Số lượng ngẫu nhiên từ 1 đến 10
        System.out.println("Đang tạo " + count + " vật thể ngẫu nhiên...");

        for (int i = 0; i < count; i++) {
            String name = "Object" + (i + 1);
            double x = random.nextDouble() * radius; // Tọa độ X ngẫu nhiên trong bán kính
            double y = random.nextDouble() * radius; // Tọa độ Y ngẫu nhiên trong bán kính
            double speed = random.nextDouble() * 100; // Vận tốc ngẫu nhiên từ 0 đến 100
            double direction = random.nextDouble() * 360; // Hướng ngẫu nhiên từ 0 đến 360 độ
            ObjectDetected obj = new ObjectDetected(name, x, y, speed, direction);
            addObject(obj);
            System.out.println("Đã tạo vật thể ngẫu nhiên: " + obj);
        }
    }
}
