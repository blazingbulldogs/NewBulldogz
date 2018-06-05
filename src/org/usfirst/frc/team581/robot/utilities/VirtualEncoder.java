package org.usfirst.frc.team581.robot.utilities;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/*
 * A class that pretends to be an encoder (by implementing the PIDSource interface),
 * but actually reads from two encoders and combines their values. "Combines" can
 * be averaging or subtraction, depending on how the operation field is set. You
 * can also set the virtual encoder to read displacement or rate (velocity) from its
 * encoders.
 */

public class VirtualEncoder implements PIDSource {

	private PIDSource source1, source2;
	private PIDSourceType sourceType;
	private VirtualEncoderOperation operation;

	public VirtualEncoder(PIDSource source1, PIDSource source2) {
		this.source1 = source1;
		this.source2 = source2;
	}

	@Override
	public double pidGet() {
		// Overwrite the encoder source type.
		source1.setPIDSourceType(sourceType);
		source2.setPIDSourceType(sourceType);
		
		if (operation == VirtualEncoderOperation.AVERAGE) {
			return (source1.pidGet() + source2.pidGet()) / 2.0;
		} else if (operation == VirtualEncoderOperation.DIFFERENCE) {
			return source1.pidGet() - source2.pidGet();
		} else {
			return 0.0;
		}
	}

	public VirtualEncoderOperation getOperation() {
		return operation;
	}

	public void setOperation(VirtualEncoderOperation operation) {
		this.operation = operation;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return this.sourceType;
	}

	@Override
	public void setPIDSourceType(PIDSourceType sourceType) {
		this.sourceType = sourceType;
	}

}
