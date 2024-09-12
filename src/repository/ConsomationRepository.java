package repository;

import domain.Consomation;
import domain.Alimentation;
import domain.Transport;
import domain.Logement;
import java.sql.*;
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
}