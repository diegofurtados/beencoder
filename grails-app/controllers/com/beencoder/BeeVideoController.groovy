package com.beencoder

import grails.converters.JSON

import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import com.beencoder.amazon.ws.AmazonS3Delegate
import com.beencoder.zencoder.ZencoderDelegate

class BeeVideoController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	ZencoderDelegate zencodeDelegate = new ZencoderDelegate()
	AmazonS3Delegate s3Delegate = new AmazonS3Delegate()

	def index() {
		render(view : "encode")
	}

	def encode() {
		MultipartFile video = request instanceof MultipartHttpServletRequest ? request.getFile("beeMovie") : null
		if (video){
			def s3Object = s3Delegate.push(video.inputStream, video.originalFilename)
			String jobId = zencodeDelegate.encode(s3Object.getName())

			redirect(action: "show", id: jobId)
		} else {
			render(view : "encode")
		}
	}

	def show(Integer id){
		def beeVideoJob = null
		beeVideoJob = zencodeDelegate.getBeeVideo(id)

		[beeVideoInstance : beeVideoJob]
	}

	def progress(Integer id){
		response.contentType = "application/json"
		render JSON.parse(zencodeDelegate.getJobProgress(id.toString())) as JSON
	}

	def list() {
		def beeJobVideos = zencodeDelegate.list()
		[beeVideoInstanceList: beeJobVideos, beeVideoInstanceTotal: beeJobVideos.size()]
	}
}
