package com.beencoder

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartFile;

class BeeVideoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        render(view : "encode")
    }

    def encode() {
		MultipartFile beeVideo = param.getFile("beeMovie")
		
    }
}
