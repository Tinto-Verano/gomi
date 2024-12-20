# voxel이 3D 공간에서 차지하는 크기
# 작을수록 정밀하지만 계산량 많고
# 크면 효율적이지만 세부정보를 잃을 수 있다.
voxel_size = [0.05, 0.05, 0.1]

model = dict(
    type='VoxelNet',
    data_preprocessor=dict(
        type='Det3DDataPreprocessor',
        # PointCloud를 Voxel로 만들
        voxel=True,
        voxel_layer=dict(
            # 각 Voxel에 저장할 최대 점 개수 
            max_num_points=5,
            point_cloud_range=[0, -40, -3, 70.4, 40, 1],
            voxel_size=voxel_size,
            # 학습 시 최대 16000개의 Voxel
            # 테스트 시 최대 40000개의 Voxel 생성
            max_voxels=(16000, 40000))),
    # Voxel 내부의 점들을 하나의 특징 벡터로 변환
    voxel_encoder=dict(type='HardSimpleVFE'),
    # Sparse Convolution Encoder
    middle_encoder=dict(
        type='SparseEncoder',
        in_channels=4,
        sparse_shape=[41, 1600, 1408],
        order=('conv', 'norm', 'act')),
    backbone=dict(
        type='SECOND',
        in_channels=256,
        layer_nums=[5, 5],
        layer_strides=[1, 2],
        out_channels=[128, 256]),
    neck=dict(
        # Featrue Pyramid Network
        type='SECONDFPN',
        in_channels=[128, 256],
        upsample_strides=[1, 2],
        out_c임
        loss_cls=dict(
            type='mmdet.FocalLoss',
            use_sigmoid=True,
            # hard sample에 더 많은 가중치를 줌
            gamma=2.0,
            # positive class에 더 높은 가중치를 부여해 클래스 불균형 보완
            alpha=0.25,
            loss_weight=1.0),
        # 오차가 작으면 L2처럼 작동 크면 L1처럼 작동하는 
        # SmoothL1Loss
        loss_bbox=dict(
            type='mmdet.SmoothL1Loss', beta=1.0 / 9.0, loss_weight=2.0),
        # CrossEntropy를 사용해 방향을 예측
        loss_dir=dict(
            type='mmdet.CrossEntropyLoss', use_sigmoid=False,
            loss_weight=0.2)),
    
    # model training and testing settings
    train_cfg=dict(
        assigner=[
            dict(  # for Pedestrian
                type='Max3DIoUAssigner',
                iou_calculator=dict(type='BboxOverlapsNearest3D'),
                pos_iou_thr=0.35,
                neg_iou_thr=0.2,
                min_pos_iou=0.2,
                ignore_iof_thr=-1),
            dict(  # for Cyclist
                type='Max3DIoUAssigner',
                iou_calculator=dict(type='BboxOverlapsNearest3D'),
                pos_iou_thr=0.35,
                neg_iou_thr=0.2,
                min_pos_iou=0.2,
                ignore_iof_thr=-1),
            dict(  # for Car
                type='Max3DIoUAssigner',
                iou_calculator=dict(type='BboxOverlapsNearest3D'),
                pos_iou_thr=0.6,
                neg_iou_thr=0.45,
                min_pos_iou=0.45,
                ignore_iof_thr=-1),
        ],
        allowed_border=0,
        pos_weight=-1,
        debug=False),
    test_cfg=dict(
        use_rotate_nms=True,
        nms_across_levels=False,
        nms_thr=0.01,
        score_thr=0.1,
        min_bbox_size=0,
        # NMS 적용 전 처리할 최대 바운딩 박스 수
        nms_pre=100,
        max_num=50))
