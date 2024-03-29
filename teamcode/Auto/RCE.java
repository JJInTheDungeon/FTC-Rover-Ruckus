/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode2019;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="RCE", group="Pushbot")
@Disabled
public class RCE extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor lf = null;
    private DcMotor lr = null;
    private DcMotor rf = null;
    private DcMotor rr = null;

    private Servo Bucket = null;
    private Servo Door = null;

    private DcMotor Lift = null;
    private DcMotor Angle = null;
    private DcMotor Slide = null;

    // DigitalChannel digitalTouch;

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.5;
    static final double     TURN_SPEED              = 0.3;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        lf = hardwareMap.get(DcMotor.class, "lf");
        lr = hardwareMap.get(DcMotor.class, "lr");
        rf = hardwareMap.get(DcMotor.class, "rf");
        rr = hardwareMap.get(DcMotor.class, "rr");

        Bucket = hardwareMap.get(Servo.class, "Bucket");
        Door = hardwareMap.get(Servo.class, "Door");

        Lift = hardwareMap.get(DcMotor.class, "Lift");
        Angle = hardwareMap.get(DcMotor.class, "Angle");
        Slide = hardwareMap.get(DcMotor.class, "Slide");

        // digitalTouch = hardwareMap.get(DigitalChannel.class, "Sensor_Touch");

        rf.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        // digitalTouch.setMode(DigitalChannel.Mode.INPUT);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          lf.getCurrentPosition(),
                          lr.getCurrentPosition(),
                          rf.getCurrentPosition(),
                          rr.getCurrentPosition());

        telemetry.update();

        waitForStart();

        // If is not pressed
        // if (digitalTouch.getState() == false) {
        //     Lift.setPower(1);
        // }
        // else {
        //     sleep(200);
        //     Lift.setPower(0);


        // Forward
        encoderDrive(TURN_SPEED, 8, 8, 8, 8, 3.0);

        // Strafe left, away from the hook
        encoderDrive(TURN_SPEED, -4, 4, 4, -4, 1.0);

        Lift.setPower(-1);
        sleep(4000);

        // Backward
        encoderDrive(DRIVE_SPEED, -12, -12, -12, -12, 3.0);

        // Strafe left, sample
        encoderDrive(TURN_SPEED, -15, 15, 15, -15, 5.0);

        // Strafe right
        encoderDrive(TURN_SPEED, 5, -5, -5, 5, 3.0);

        // Backward, almost to the wall
        encoderDrive(DRIVE_SPEED, -20, -20, -20, -20, 5.0);

        // Turn left 135
        encoderDrive(TURN_SPEED, 10, 10, -10, -10, 3.0);

        // Strafe right, aline parallel to wall
        encoderDrive(TURN_SPEED, 5, -5, -5, 5, 3.0);

        // Strafe left, no wall touchy
        encoderDrive(TURN_SPEED, -3, 3, 3, -3, 3.0);

        // Forward, to the depot
        encoderDrive(TURN_SPEED, 20, 20, 20, 20, 3.0);


        // Dump the totem
        Bucket.setPosition(.94);
        sleep(200);

        // Retract servo back up
        Bucket.setPosition(.75);
        sleep(300);

        // May need the wall check up before heading backward

        // Backward
        encoderDrive(DRIVE_SPEED, -12, -12, -12, -12, 3.0);

        // Turn right 180
        encoderDrive(TURN_SPEED, -15, -15, 15, 15, 5.0);

        // Strafe left, aline parallel to wall
        encoderDrive(TURN_SPEED, -7, 7, 7, -7, 3.0);

        // Strafe right, aline parallel to wall
        encoderDrive(TURN_SPEED, 4, -4, -4, 4, 3.0);

        // Forward, to park
        encoderDrive(DRIVE_SPEED, 20, 20, 20, 20, 5.0);

        //Park on crater
        Bucket.setPosition(.94);
        sleep(200);


        // Done!
        sleep(200);

        // }
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftFInches, double leftRInches, double rightFInches, double rightRInches,
                             double timeoutS) {
        int newLeftFTarget;
        int newLeftRTarget;
        int newRightFTarget;
        int newRightRTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFTarget = lf.getCurrentPosition() + (int)(leftFInches * COUNTS_PER_INCH);
            newLeftRTarget = lr.getCurrentPosition() + (int)(leftRInches * COUNTS_PER_INCH);
            newRightFTarget = rf.getCurrentPosition() + (int)(rightFInches * COUNTS_PER_INCH);
            newRightRTarget = rr.getCurrentPosition() + (int)(rightRInches * COUNTS_PER_INCH);

            lf.setTargetPosition(newLeftFTarget);
            lr.setTargetPosition(newLeftRTarget);
            rf.setTargetPosition(newRightFTarget);
            rr.setTargetPosition(newRightRTarget);

            // Turn On RUN_TO_POSITION
            lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rr.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            lf.setPower(Math.abs(speed));
            lr.setPower(Math.abs(speed));
            rf.setPower(Math.abs(speed));
            rr.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                  (runtime.seconds() < timeoutS) &&
                  (lf.isBusy() && lr.isBusy() && rf.isBusy() && rr.isBusy())) {

                // Display it for the driver.
    telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFTarget, newLeftRTarget, newRightFTarget, newRightRTarget);
    telemetry.addData("Path2",  "Running at %7d :%7d",
                                            lf.getCurrentPosition(),
                                            lr.getCurrentPosition(),
                                            rf.getCurrentPosition(),
                                            rr.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            lf.setPower(0);
            lr.setPower(0);
            rf.setPower(0);
            rr.setPower(0);

            // Turn off RUN_TO_POSITION
            lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
