public class ObjectDetected {
    private String name;
    private double x, y;  // Tọa độ
    private double speed; // Vận tốc
    private double direction; // Hướng di chuyển (theo độ)

    public ObjectDetected(String name, double x, double y, double speed, double direction) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance() {
        return Math.sqrt(x * x + y * y);
    }

    public String getName() {
        return name;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    // Cập nhật vị trí mới của vật thể dựa trên vận tốc và hướng
    public void updatePosition(double deltaTime) {
        double radians = Math.toRadians(direction);
        x += speed * deltaTime * Math.cos(radians);
        y += speed * deltaTime * Math.sin(radians);
    }

    public void updatePosition(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    @Override
    public String toString() {
        return String.format("Vật thể: %s | Vị trí: (%.2f, %.2f) | Khoảng cách: %.2f | Vận tốc: %.2f | Hướng: %.2f°",
                name, x, y, getDistance(), speed, direction);
    }
}
