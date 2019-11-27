package frc.robot;

import java.io.IOException;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.robot.subsystems.*;
import frc.robot.commands.*;
import frc.robot.helpers.*;
import frc.robot.stateEstimation.*;
import frc.robot.logging.*;
import frc.robot.shapes.*;
import frc.robot.logging.Logger.DefaultValue;
import frc.robot.RobotState.SD;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
    private OutputLogger outputLogger;
    private Timer timer = new Timer();
    public static Logger logger = new Logger();

    public static RobotStateHistory stateHistory = new RobotStateHistory();
    
    public static DriveSubsystem      driveSubsystem      = new DriveSubsystem();
    public static GearShiftSubsystem  gearShiftSubsystem  = new GearShiftSubsystem();
    public static EncoderSubsystem    encoderSubsystem    = new EncoderSubsystem();
    public static LimelightSubsystem  limelightSubsystem  = new LimelightSubsystem();
    public static PneumaticsSubsystem pneumaticsSubsystem = new PneumaticsSubsystem();
    
    public static Following following     = new Following();
    public static EncoderLocalization positionModel = new EncoderLocalization();
    public static CircleController circleController = new CircleController();
    public static OI oi = new OI();

    public static RobotState getState(){ return stateHistory.currentState(); }

    public static double get(SD sd)            {return getState().get(sd);}
    public static void   set(SD sd, double val){       getState().set(sd, val);}
    public static Value getSolenoidValue(SD sd){return getState().getSolenoidValue(sd);}

    public static Point  getPos() {return new Point(get(SD.X),get(SD.Y));}
    public static double getHeading() {return get(SD.Angle);}
    public static final Point TEST_POINT = new Point (3, 4);
    public static final double TEST_HEADING = Math.PI * .1;
    public static final Point ORIGINAL_POINT = new Point(0,0);
    public static final double ORIGINAL_ANGLE = Geo.HORIZONTAL_ANGLE;
    public static final int INTERVAL = 50; // change this to change your frequency of prints
    public static double numTicks = 0;     // rmbr that each tick is .02 seconds, 50 ticks/second

    private int attemptsSinceLastLog;
    public static final int LOG_PERIOD = 5;

    // private void allPeriodicLogs() {
    //     driveSubsystem.periodicLog();
    //     gearShiftSubsystem.periodicLog();
    //     limelightSubsystem.periodicLog();
    //     pneumaticsSubsystem.periodicLog();
    //     encoderSubsystem.periodicLog();
    //     following.periodicLog();
    // }
    private void allUpdateRobotStates() {
        driveSubsystem.updateRobotState();
        gearShiftSubsystem.updateRobotState();
        pneumaticsSubsystem.updateRobotState();
    }

    public void useModel(Model model){
        stateHistory.currentState().incorporateIntoNew(model.updatedRobotState(), model.getSDs());
    }

    public static void delayedPrint (String message) {
        if (numTicks % INTERVAL == 0) {
            System.out.println(message);
        }
    }

    public void robotInit() {
        try {
            outputLogger = new OutputLogger("/home/lvuser/Logs/output_log.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to initialize OutputLogger");
        }
        timer.start();
        // attemptsSinceLastLog = 0;
        set(SD.X, ORIGINAL_POINT.x);
        set(SD.Y, ORIGINAL_POINT.y);
        set(SD.Angle, ORIGINAL_ANGLE);
        allUpdateRobotStates();
        useModel(positionModel);
        pneumaticsSubsystem.stopCompressor();
        //logger.incrementPrevious("robot.java", "deploy", DefaultValue.Previous);

        /* STARTS THE LIDAR     
        try {
            System.out.println("LIDAR status: starting");
            boolean started = LidarServer.getInstance().start();
            System.out.println("LIDAR status" + (started ? "started" : "failed to start"));
        } catch (Throwable t) {
            System.out.println("LIDAR status: crashed -" + t);
            t.printStackTrace();
            throw t;
        }*/

        //logger.logData();
    }

    public void logDataPeriodic() {
        // if (LOG_PERIOD == attemptsSinceLastLog) {
        //     attemptsSinceLastLog = 0;
        //     allPeriodicLogs();
        //     logger.logData();
        // } else {
        //     attemptsSinceLastLog++;
        // }
    }
 
    public void robotPeriodic() {
        double time = timer.get();
        if (time <= 15) {
            try {
                outputLogger.logToFile(time);
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to log @ " + time + "s");
            }
        } else {
            try {
                outputLogger.destroyWriter();
            } catch (IOException e) {
                System.out.println("[ERROR] Failed to destroy BufferedWriter");
            }
            timer.stop();
        }
        numTicks += 1;
        allUpdateRobotStates();
        Scheduler.getInstance().run();
        stateHistory.addState(getState().copy());
        useModel(positionModel);
        //delayedPrint("X: " + Robot.get(SD.X) + "\nY: " + Robot.get(SD.Y) + "\nAngle: " + Math.toDegrees(Robot.get(SD.Angle)));
    }
        
    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
        // new SwerveTankDriveCommand().start();
        // circleController.update(getPos(), getHeading(), TEST_POINT, TEST_HEADING);
        pneumaticsSubsystem.startCompressor();
        enabledPeriodic();
    }
    
    @Override
    public void teleopInit() {
    }

    public void teleopPeriodic() {
        new TankDriveCommand().start();
        pneumaticsSubsystem.startCompressor();
        enabledPeriodic();
    }

    public void testPeriodic() {
        enabledPeriodic();
    }

    public void enabledPeriodic() {logDataPeriodic();}

    public void disabledInit() {
        // allPeriodicLogs();
        // logger.logData();
        // logger.writeLoggedData();
    }
}
