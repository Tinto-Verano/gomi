# 초기 학습률
lr = 0.0018

# The optimizer follows the setting in SECOND.Pytorch, but here we use
# the official AdamW optimizer implemented by PyTorch.
optim_wrapper = dict(
    type='OptimWrapper',
    optimizer=dict(type='AdamW', lr=lr, betas=(0.95, 0.99), weight_decay=0.01),
    clip_grad=dict(max_norm=10, norm_type=2))
# learning rate
param_scheduler = [
    # learning rate scheduler
    # During the first 16 epochs, learning rate increases from 0 to lr * 10
    # during the next 24 epochs, learning rate decreases from lr * 10 to
    # lr * 1e-4
    # 16 에포크까지
    dict(
        # 학습률을 코사인 곡선을 따라 조정
        type='CosineAnnealingLR',
        T_max=16,
        eta_min=lr * 10,
        begin=0,
        end=16,
        by_epoch=True,
        convert_to_iter_based=True),
    # 40 에포크까지
    dict(
        type='CosineAnnealingLR',
        T_max=24,
        eta_min=lr * 1e-4,
        begin=16,
        end=40,
        by_epoch=True,
        convert_to_iter_based=True),
    
    # momentum scheduler
    # During the first 16 epochs, momentum increases from 0 to 0.85 / 0.95
    # during the next 24 epochs, momentum increases from 0.85 / 0.95 to 1
    dict(
        type='CosineAnnealingMomentum',
        T_max=16,
        eta_min=0.85 / 0.95,
        begin=0,
        end=16,
        by_epoch=True,
        convert_to_iter_based=True),
    dict(
        type='CosineAnnealingMomentum',
        T_max=24,
        eta_min=1,
        begin=16,
        end=40,
        by_epoch=True,
        convert_to_iter_based=True)
]

# Runtime settings，training schedule for 40e
# Although the max_epochs is 40, this schedule is usually used we
# RepeatDataset with repeat ratio N, thus the actual max epoch
# number could be Nx40
train_cfg = dict(by_epoch=True, max_epochs=40, val_interval=1)
val_cfg = dict()
test_cfg = dict()

auto_scale_lr = dict(enable=False, base_batch_size=48)
