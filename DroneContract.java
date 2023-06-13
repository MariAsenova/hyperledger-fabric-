import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Contract(name = "DroneContract",
        info = @Info(title = "Drone contract",
                description = "Basic Java contract for managing military drones' surveillance data",
                version = "0.0.1"))
@Default
public class DroneContract implements ContractInterface {

    @Transaction
    public Drone registerDrone(Context ctx, String droneId) {
        ChaincodeStub stub = ctx.getStub();
        
        String droneState = stub.getStringState(droneId);
        if (droneState != null && !droneState.isEmpty()) {
            String errorMessage = String.format("Drone %s already exists", droneId);
            System.out.println(errorMessage);
            return null;
        }

        Drone drone = new Drone(droneId);
        String droneStateData = Context.toJson(drone);
        stub.putStringState(droneId, droneStateData);

        return drone;
    }

    @Transaction
    public Drone recordSurveillanceData(Context ctx, String droneId, String surveillanceData) {
        ChaincodeStub stub = ctx.getStub();
        
        String droneState = stub.getStringState(droneId);
        if (droneState == null || droneState.isEmpty()) {
            String errorMessage = String.format("Drone %s does not exist", droneId);
            System.out.println(errorMessage);
            return null;
        }

        Drone drone = Context.fromJson(droneState, Drone.class);
        drone.addSurveillanceData(surveillanceData);
        String updatedDroneStateData = Context.toJson(drone);
        stub.putStringState(droneId, updatedDroneStateData);

        return drone;
    }

    @Transaction
    public Drone getSurveillanceData(Context ctx, String droneId) {
        ChaincodeStub stub = ctx.getStub();
        
        String droneState = stub.getStringState(droneId);
        if (droneState == null || droneState.isEmpty()) {
            String errorMessage = String.format("Drone %s does not exist", droneId);
            System.out.println(errorMessage);
            return null;
        }

        Drone drone = Context.fromJson(droneState, Drone.class);
        return drone;
    }
}
