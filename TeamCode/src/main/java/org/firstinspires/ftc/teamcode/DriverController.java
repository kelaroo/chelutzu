package org.firstinspires.ftc.teamcode;

import android.widget.AbsListView;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;

@TeleOp(name = "CHELUTZU")

public class DriverController extends OpMode {

    private static final double COEFF_HEAD = 0.2;
    private static final double COEFF_DRIVE = 1;
    private static final double COEFF_ROTATE = 0.7;
    private static final double ARM_NEG_POWER = -0.1;
    private static final double ARM_POS_POWER = 0.1;

    private static final long SERVO_TICK_TIME = 100;
    private static final double POS_PER_TICK = 0.15;

    private static final long AUTO_TIME_RESET = 30;

    private int waveStage = 0;
    private HardwareConfig hw;

    private ElapsedTime sinceLeftServo = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private ElapsedTime sinceRightServo = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private ElapsedTime timeSinceAuto = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    @Override
    public void init() {
        hw = new HardwareConfig(hardwareMap);
        telemetry.addData("Status", "Chelutzu initialized");

        sinceLeftServo.reset();
        sinceRightServo.reset();
        timeSinceAuto.reset();
    }

    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double rotate = gamepad1.right_stick_x;
        double headRight = gamepad1.right_trigger;
        double headLeft = -gamepad1.left_trigger;

        // CAPUL PULII
        hw.head.setPower((headRight + headLeft) * COEFF_HEAD);

        // DRIVE
        if(rotate != 0) {
            hw.leftDrive.setPower(-rotate * COEFF_ROTATE);
            hw.rightDrive.setPower(rotate * COEFF_ROTATE);
        }
        else {
            hw.leftDrive.setPower(drive * COEFF_DRIVE);
            hw.rightDrive.setPower(drive * COEFF_DRIVE);
        }

        // ARMS
        if(gamepad1.dpad_left) {
            hw.armLeft.setPower(ARM_NEG_POWER);
        }
        else if(gamepad1.dpad_right) {
            hw.armLeft.setPower(ARM_POS_POWER);
        }
        else {
            hw.armLeft.setPower(0);
        }

        if(gamepad1.x) {
            hw.armRight.setPower(ARM_NEG_POWER);
        }
        else if(gamepad1.b) {
            hw.armRight.setPower(ARM_POS_POWER);
        }
        else {
            hw.armRight.setPower(0);
        }

        // ELBOWS
        double leftServo = gamepad1.left_stick_x;
        double rightServo = -gamepad1.right_stick_y;

        if(timeSinceAuto.time() > AUTO_TIME_RESET) {
            hw.elbowLeft.setPosition(0);
            hw.elbowRight.setPosition(1);
            timeSinceAuto.reset();
            waveStage = 0;
            telemetry.addData("Chelutzu", "Wave init");
        }
        else if(timeSinceAuto.time() > 2 && waveStage == 0) {
            hw.elbowRight.setPosition(0.3);
            waveStage = 1;
            telemetry.addData("Chelutzu", "Wave motion 1");
        }
        else if(timeSinceAuto.time() > 2.2 && waveStage == 1) {
            hw.elbowRight.setPosition(0);
            hw.elbowLeft.setPosition(0.5);
            waveStage = 2;
            telemetry.addData("Chelutzu", "Wave motion 2");
        }
        else {
            if (leftServo != 0 && sinceLeftServo.time() > SERVO_TICK_TIME) {
                double toGoPos = hw.elbowLeft.getPosition() + (leftServo * POS_PER_TICK);
                if (toGoPos > 1) {
                    toGoPos = 1;
                    telemetry.addData("elbowLeft", "Can't go beyond 1.");
                } else if (toGoPos < 0) {
                    toGoPos = 0;
                    telemetry.addData("elbowLeft", "Can't go beyond 0.");
                }
                hw.elbowLeft.setPosition(toGoPos);
                sinceLeftServo.reset();
            } else {
                if (gamepad1.dpad_down) {
                    hw.elbowLeft.setPosition(1);
                } else if (gamepad1.dpad_up) {
                    hw.elbowLeft.setPosition(0);
                } else if (gamepad1.left_bumper) {
                    hw.elbowLeft.setPosition(0.45);
                }
            }

            if (rightServo != 0 && sinceRightServo.time() > SERVO_TICK_TIME) {
                double toGoPos = hw.elbowRight.getPosition() + (rightServo * POS_PER_TICK);
                if (toGoPos > 1) {
                    toGoPos = 1;
                    telemetry.addData("elbowRight", "Can't go beyond 1.");
                } else if (toGoPos < 0) {
                    toGoPos = 0;
                    telemetry.addData("elbowRight", "Can't go beyond 0.");
                }
                hw.elbowRight.setPosition(toGoPos);
                sinceRightServo.reset();
            } else {
                if (gamepad1.a) {
                    hw.elbowRight.setPosition(0);
                } else if (gamepad1.y) {
                    hw.elbowRight.setPosition(1);
                } else if (gamepad1.right_bumper) {
                    hw.elbowRight.setPosition(0.45);
                }
            }
        }
    }
}
