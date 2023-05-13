package com.acme.sk8.stl

import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Environment
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import java.io.File
import java.io.IOException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/*
* Copyright 2017 Dmitry Brant. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
class ModelRenderer(private val model: Model?, private val surfaceView: SurfaceView) : GLSurfaceView.Renderer{
    private val light = Light(floatArrayOf(0.0f, 0.0f, MODEL_BOUND_SIZE * 10, 1.0f))
    private val floor = Floor()

    private val floorMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private var rotateAngleX = 0f
    private var rotateAngleY = 0f
    private var rotateAngleZ = 0f
    private var translateX = 0f
    private var translateY = 0f
    private var translateZ = 0f

    private val TAG = javaClass.simpleName

    private fun log(s: String) {
        Log.i(TAG, s)
    }

    private var recorder: MediaRecorder? = null
    private var recording = false
    private var mSurface : Surface? = null

    fun translate(dx: Float, dy: Float, dz: Float) {
        val translateScaleFactor = MODEL_BOUND_SIZE / 200f
        translateX += dx * translateScaleFactor
        translateY += dy * translateScaleFactor
        if (dz != 0f) {
            translateZ /= dz
        }
        updateViewMatrix()
    }

    fun rotate(aX: Float, aY: Float, aZ: Float) {
        val rotateScaleFactor = 0.5f
        rotateAngleX -= aX * rotateScaleFactor
        rotateAngleY += aY * rotateScaleFactor
        rotateAngleZ += aZ * rotateScaleFactor
        updateViewMatrix()
    }

    private fun updateViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, translateZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.translateM(viewMatrix, 0, -translateX, -translateY, 0f)
        Matrix.rotateM(viewMatrix, 0, rotateAngleX, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, rotateAngleY, 0f, 1f, 0f)
        Matrix.rotateM(viewMatrix, 0, rotateAngleZ, 0f, 0f, 1f)
    }
    private fun updateFloorMatrix() {
        Matrix.setLookAtM(floorMatrix, 0, 0f, 0f, translateZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.translateM(floorMatrix, 0, -translateX, -translateY, 0f)
        Matrix.rotateM(floorMatrix, 0, rotateAngleX, 1f, 0f, 0f)
        Matrix.rotateM(floorMatrix, 0, rotateAngleY, 0f, 1f, 0f)
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        floor.draw(floorMatrix, projectionMatrix, light)
        model?.draw(viewMatrix, projectionMatrix, light)
    }


    private fun initRecorder() { // this takes care of all the mediarecorder settings
        val video = Environment.getExternalStorageDirectory().absolutePath + "/Movies/"
        val filename = video + "sk005.mp4"
        val outputFile = File(filename)
        outputFile.createNewFile()
        recorder = MediaRecorder()
        log("Output file name: " + filename)
        val cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
//        recorder?.surface
//        recorder?.setProfile(cpHigh)

        recorder?.setVideoSource(MediaRecorder.VideoSource.SURFACE)

        mSurface?.let { recorder?.setInputSurface(it) }
//        recorder?.setIn
        //recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // default microphone to be used for audio
        // recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// default camera to be used for video capture.
//        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // generally used also includes h264 and best for flash
         recorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264); //well known video codec used by many including for flash
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// typically amr_nb is the only codec for mobile phones so...

        recorder?.setVideoFrameRate(15);// typically 12-15 best for normal use. For 1080p usually 30fms is used.
         recorder?.setVideoSize(720,480);// best size for resolution.
        //recorder.setMaxFileSize(10000000);
        recorder?.setOutputFile(outputFile)
//        recorder?.setVideoEncodingBitRate(256000);//
        //recorder.setAudioEncodingBitRate(8000);
        recorder?.setMaxDuration(600000)
    }
    private fun prepareRecorder() {
//        recorder?.setVideoSource(this.surface)
        try {
            recorder?.setPreviewDisplay(surfaceView.holder.surface)

            recorder?.prepare()
            recorder?.start()

            recording = true
        } catch (e: IllegalStateException) {
            e.printStackTrace()
//            finish()
        } catch (e: IOException) {
            e.printStackTrace()
//            finish()
        }
    }



    private fun startRecord(){
        initRecorder()
        log("Starting 3D Record")
        prepareRecorder()
    }
    public fun finishRecord(vname: String){
        if (recording) {
            log("Stopping 3D Record " + vname)
            recorder?.stop()
            recording = false
        }
        recorder?.release()
        log(" 3D Record done..")
//        finish()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, Z_NEAR, Z_FAR)

        // initialize the view matrix
        rotateAngleX = 0f
        rotateAngleY = 0f
        translateX = 0f
        translateY = 0f
        translateZ = -MODEL_BOUND_SIZE * 1.5f
        updateViewMatrix()

        // Set light matrix before doing any other transforms on the view matrix
        light.applyViewMatrix(viewMatrix)

        // By default, rotate the model towards the user a bit
        rotateAngleX = -24.0f
        rotateAngleY = 65.0f
        updateViewMatrix()
        updateFloorMatrix()
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        //GLES20.glEnable(GLES20.GL_BLEND);
        //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        floor.setup(MODEL_BOUND_SIZE)
        model?.let {
            it.setup(MODEL_BOUND_SIZE)
            floor.setOffsetY(it.floorOffset)
        }
        updateFloorMatrix()
//        startRecord()
    }

    companion object {
        private const val MODEL_BOUND_SIZE = 50f
        private const val Z_NEAR = 2f
        private const val Z_FAR = MODEL_BOUND_SIZE * 10
    }

}
