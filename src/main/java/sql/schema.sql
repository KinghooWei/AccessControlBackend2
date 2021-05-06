CREATE TABLE passenger (
    `flight` varchar(20) NOT NULL COMMENT '航班',
    `date` date NOT NULL COMMENT '日期',
    `from` varchar(20) NOT NULL COMMENT '始发地',
    `to` varchar(20) NOT NULL COMMENT '目的地',

    `passenger_id` varchar(30) NOT NULL COMMENT '身份识别号',
    `passenger_name` varchar(10) NOT NULL COMMENT '姓名',
    `passenger_face` MEDIUMBLOB NOT NULL COMMENT '人脸',
    `face_feature` varchar(1280) NOT NULL COMMENT '人脸特征',
    `feature_with_mask` varchar(1280) NOT NULL COMMENT '戴口罩的人脸特征',
    `flight_class` char(1) NOT NULL COMMENT '舱位',
    `seat` varchar(10) NOT NULL COMMENT '座位',
    `gate` tinyint NOT NULL COMMENT '登机口',

    PRIMARY KEY (`date`,`flight`,`passenger_id`),
    key idx_date(`date`),
    key idx_flight_number(`flight`),
    key idx_passenger_id(`passenger_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='航班乘客表';

CREATE TABLE keyPersonnel (
    `identify_id` varchar(30) NOT NULL COMMENT '身份证',
    `name` varchar(10) NOT NULL COMMENT '姓名',
    `face` MEDIUMBLOB NOT NULL COMMENT '人脸',
    `face_feature` varchar(1280) NOT NULL COMMENT '人脸特征',
    `feature_with_mask` varchar(1280) NOT NULL COMMENT '戴口罩的人脸特征',
    `gender` varchar(5) NOT NULL COMMENT '性别',
    `birth` date NOT NULL COMMENT '出生日期',
    `type` varchar(20) NOT NULL COMMENT '重点人员类别',
    `case` varchar(200) NOT NULL COMMENT '案底',

    PRIMARY KEY (`identify_id`),
    key idx_identify_id(`identify_id`),
    key idx_name(`name`),
    key idx_type(`type`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='重点人员表';

CREATE TABLE `account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8

CREATE TABLE `attendance` (
  `person_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `phoneNum` varchar(20) NOT NULL COMMENT '手机',
  `community` varchar(100) NOT NULL COMMENT '小区',
  `building` varchar(20) NOT NULL COMMENT '楼门',
  `method` varchar(20) NOT NULL COMMENT '过闸方式',
  `enterTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '过闸时间',
  `face` mediumblob COMMENT '加密人脸base64码',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `privateKey` varchar(20) DEFAULT NULL COMMENT '密钥',
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8

CREATE TABLE `attendance_card` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `phoneNum` varchar(30) NOT NULL COMMENT '手机',
  `community` varchar(20) NOT NULL COMMENT '小区',
  `building` varchar(20) NOT NULL COMMENT '楼门',
  `face` mediumblob NOT NULL COMMENT '人脸base64码',
  `longitude` varchar(20) DEFAULT NULL COMMENT '经度',
  `latitude` varchar(20) DEFAULT NULL COMMENT '纬度',
  `enterTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '过闸时间',
  PRIMARY KEY (`id`),
  KEY `index_phoneNum` (`phoneNum`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8

CREATE TABLE `person` (
  `person_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `phoneNum` varchar(20) NOT NULL,
  `loginPassword` varchar(20) NOT NULL,
  `community` varchar(100) DEFAULT NULL,
  `building` varchar(20) DEFAULT NULL,
  `roomId` varchar(10) DEFAULT NULL,
  `communityPassword` varchar(6) DEFAULT NULL,
  `buildingPassword` varchar(6) DEFAULT NULL,
  `tempPassword` varchar(6) DEFAULT NULL,
  `portrait` mediumblob COMMENT '头像',
  `faceImg` mediumblob COMMENT '人脸照片',
  `faceFeature` varchar(1280) DEFAULT NULL COMMENT '人脸128维特征',
  `featureWithMask` varchar(1280) DEFAULT NULL COMMENT '生成口罩的128维特征',
  `featureWithGlasses` varchar(1280) DEFAULT NULL COMMENT '戴墨镜的特征',
  `QRCode` varchar(8) DEFAULT NULL COMMENT '二维码内容',
  `cardID` varchar(30) DEFAULT NULL COMMENT '卡片序列号',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8