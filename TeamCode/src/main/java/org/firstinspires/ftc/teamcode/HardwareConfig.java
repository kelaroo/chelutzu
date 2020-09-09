package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareConfig {
    public DcMotor leftDrive;
    public DcMotor rightDrive;

    public DcMotor armLeft;
    public DcMotor armRight;

    public DcMotor head;

    public Servo elbowLeft;
    public Servo elbowRight;


    public HardwareConfig(HardwareMap hw) {
        leftDrive = hw.get(DcMotor.class, "leftDrive");
        rightDrive = hw.get(DcMotor.class, "rightDrive");

        armLeft = hw.get(DcMotor.class, "armLeft");
        armRight = hw.get(DcMotor.class, "armRight");

        head = hw.get(DcMotor.class, "capulpulii");

        elbowLeft = hw.get(Servo.class, "elbowLeft");
        elbowRight = hw.get(Servo.class, "elbowRight");


        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        armLeft.setDirection(DcMotor.Direction.REVERSE);
        armRight.setDirection(DcMotor.Direction.FORWARD);

        elbowRight.setPosition(0.45);
        elbowLeft.setPosition(0.45);
    }
}
