@Override
public Optional<Integer> save(Vehicle vehicle) {
    String sql = "INSERT INTO vehicles(vehicle_type, license_plate, status, current_location) VALUES(?,?,?,?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, vehicle.getVehicleType());
        pstmt.setString(2, vehicle.getLicensePlate());
        pstmt.setString(3, vehicle.getStatus());
        pstmt.setString(4, vehicle.getCurrentLocation());
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) return Optional.empty();

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return Optional.of(generatedKeys.getInt(1));
            } else {
                return Optional.empty();
            }
        }
    } catch (SQLException e) {
        throw new DataAccessException("Error saving vehicle.", e);
    }
}
