package repository;

import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsomationRepository {
    private Connection connection;

    public ConsomationRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<Consomation> addConsomation(Consomation consomation) {
        String sql = null;
        if (consomation instanceof Alimentation) {
            sql = "INSERT INTO alimentations (value, startdate, enddate, types_consommation_id, user_id, type_aliment, poids) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consomation instanceof Logement) {
            sql = "INSERT INTO logement (value, startdate, enddate, types_consommation_id, user_id, type_energie, consommation_energie) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (consomation instanceof Transport) {
            sql = "INSERT INTO transport (value, startdate, enddate, types_consommation_id, user_id, distance, type_transport) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        if (sql != null) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, consomation.getValue());
                ps.setDate(2, Date.valueOf(consomation.getStartDate()));
                ps.setDate(3, Date.valueOf(consomation.getEndDate()));
                ps.setInt(4, consomation.getType());
                ps.setInt(5, consomation.getUser().getId());

                if (consomation instanceof Alimentation) {
                    Alimentation alimentation = (Alimentation) consomation;
                    ps.setString(6, alimentation.getTypeA().toString());
                    ps.setDouble(7, alimentation.getPoids());
                } else if (consomation instanceof Logement) {
                    Logement logement = (Logement) consomation;
                    ps.setString(6, logement.getTypeE().toString());
                    ps.setDouble(7, logement.getEnergie());
                } else if (consomation instanceof Transport) {
                    Transport transport = (Transport) consomation;
                    ps.setDouble(6, transport.getDistance());
                    ps.setString(7, transport.getTypeV().toString());
                }

                ps.executeUpdate();
                return Optional.of(consomation);
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public List<Consomation> getUserConsomations(User user) throws SQLException {
        List<Consomation> consomations = new ArrayList<>();

        String transportQuery = "SELECT * FROM transport JOIN consomations ON transport.id = consomations.id WHERE consomations.user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(transportQuery)) {
            pstmt.setInt(1, user.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TypeVehicule typeVehicule = TypeVehicule.valueOf(rs.getString("type").toUpperCase());
                    Transport transport = new Transport(
                            rs.getInt("value"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getDouble("distance"),
                            typeVehicule,
                            1
                    );
                    transport.setUser(user);
                    consomations.add(transport);
                }
            }
        }

        String logementQuery = "SELECT * FROM logement JOIN consomations ON logement.id = consomations.id WHERE consomations.user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(logementQuery)) {
            pstmt.setInt(1, user.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TypeEnergie typeEnergie = TypeEnergie.valueOf(rs.getString("type").toUpperCase());
                    Logement logement = new Logement(
                            rs.getInt("value"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            typeEnergie, // Energy type
                            rs.getDouble("energie"),
                            2
                    );
                    logement.setUser(user);
                    consomations.add(logement);
                }
            }
        }

        String alimentationQuery = "SELECT * FROM alimentation JOIN consomations ON alimentation.id = consomations.id WHERE consomations.user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(alimentationQuery)) {
            pstmt.setInt(1, user.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TypeAliment typeAliment = TypeAliment.valueOf(rs.getString("type").toUpperCase());
                    Alimentation alimentation = new Alimentation(
                            rs.getInt("value"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("end_date").toLocalDate(),
                            rs.getDouble("poids"),
                            typeAliment,
                            3
                    );
                    alimentation.setUser(user);
                    consomations.add(alimentation);
                }
            }
        }

        return consomations;
    }

}