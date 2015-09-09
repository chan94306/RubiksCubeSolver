package solver;

public interface CameraImageProcessor {

	byte[] getImageYUV();

	void initImageDimensions(int width, int height);

	void initImageYUV(int length);

	// TODO: this is kinda of sketch because it should be inherited form View
	void invalidate();

}
