    public boolean updateQuantity(int productId, int newQuantity) {
        String sql = "UPDATE product SET quantity =? WHERE product_id =?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating product quantity.", e);
        }
    }
