package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.util.Range;

public class Autonomous extends LinearOpMode {
    DcMotor leftfrontMotor;
    DcMotor leftbackMotor;
    DcMotor rightfrontMotor;
    DcMotor rightbackMotor;
    ModernRoboticsI2cGyro sensorGyro;

    @Override
    public void runOpMode() throws InterruptedException {
        leftfrontMotor =hardwareMap.dcMotor.get("leftfront_motor");
        leftbackMotor =hardwareMap.dcMotor.get("leftback_motor");
        rightfrontMotor = hardwareMap.dcMotor.get("rightfront_motor");
        rightbackMotor = hardwareMap.dcMotor.get("rightback_motor");

        //rightfrontMotor.setDirection(DcMotor.Direction.REVERSE);
        //rightbackMotor.setDirection(DcMotor.Direction.REVERSE);

        leftfrontMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftbackMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rightfrontMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rightbackMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        sensorGyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        sensorGyro.calibrate();

        waitForStart();

        //tankDrive(leftY, rightY, sleep for how many milsec)
        tankDrive(1.0, 1.0, 500);

        //turn 45 degrees
        //turnAngle(-45.0);
        tankDrive(-0.5, 0.5, 500);

        //move forward for 2500 ms
        tankDrive(1.0, 1.0, 1500);

        //turn 90 degrees
        //turnAngle(-90.0);
        tankDrive(-0.5, 0.5, 1000);

        //move forward for 1000 ms
        tankDrive(1.0, 1.0, 750);

    }

    private void turnAngle(double theta) throws InterruptedException {
        /*leftfrontMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftbackMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rightfrontMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        rightbackMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);*/

        double e = theta;
        double k = 1.0/90.0;
        double a;

        telemetry.addData("Heading", sensorGyro.getHeading());
        sensorGyro.calibrate();
        while(sensorGyro.isCalibrating());


        while(e > 0.1 || e < -0.1){
            a = sensorGyro.getHeading();
            if (a > 180){
                a = a - 360;
            }
            telemetry.addData("Heading", a);
            e = theta - a;
            telemetry.addData("error", e);
            double u = e * k;
            u = Range.clip(u,-1, 1);
            telemetry.addData("error", e);

            leftfrontMotor.setPower(u);
            leftbackMotor.setPower(u);
            rightfrontMotor.setPower(-u);
            rightbackMotor.setPower(-u);
        }
        leftfrontMotor.setPower(0.0);
        leftbackMotor.setPower(0.0);
        rightfrontMotor.setPower(0.0);
        rightbackMotor.setPower(0.0);

    }

    private void tankDrive(double leftY, double rightY) throws InterruptedException {
        leftfrontMotor.setPower(leftY);
        leftbackMotor.setPower(leftY);
        rightfrontMotor.setPower(-rightY);
        rightbackMotor.setPower(-rightY);

    }

    private void tankDrive(double leftY, double rightY, long sleepAmount) throws InterruptedException {
        tankDrive(leftY, rightY);
        sleep(sleepAmount);
        tankDrive(0.0, 0.0);

    }

    /*private void moveDistance(double distance) throws InterruptedException {

        leftMotor.setPower(1.0);
        rightMotor.setPower(1.0);
        sleep(distance);


        leftMotor.setMode(DcMotorController.RunMode .RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        leftMotor.setTargetPosition((int)(distance*1440));
        rightMotor.setTargetPosition((int)(distance*1440));

        leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
       rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(1.0);
        rightMotor.setPower(1.0);

        while(leftMotor.isBusy()&& rightMotor.isBusy());

        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);

    }*/
}