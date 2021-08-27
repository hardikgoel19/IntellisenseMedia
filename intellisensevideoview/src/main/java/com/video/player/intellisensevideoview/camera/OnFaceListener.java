package com.video.player.intellisensevideoview.camera;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;

public interface OnFaceListener {
    void onface(Detector.Detections<Face> detections, Face face);
    void noface(Detector.Detections<Face> detections);
}
