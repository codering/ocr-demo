# Tesseract 自定义训练数据步骤

- 安装 [tesseract](https://tesseract-ocr.github.io/tessdoc/Installation.html)，方便在命令行中使用
- 下载 [jTessBoxEditor](https://github.com/nguyenq/jTessBoxEditor/releases), 同于训练数据
- 将要训练的图片格式转换为 tif 或 tiff 格式
- 打开 jTessBoxEditor -> 菜单 Tools -> Merge TIFF，出现一个弹框,选择所有图片，点击"打开"
- 打开后，会再次出现一个弹框，输入名字 num.font.exp0.tif, 点击“保存”
- 执行命令, 生成box文件
  ```shell
  # cd 命令进入tif图片目录
  tesseract num.font.exp0.tif num.font.exp0 batch.nochop makebox
  ```
- 创建 font_properties， 这里的 font 命名来自 `num.font.exp0.tif` 中的 font
  ```shell
  echo 'font 0 0 0 0 0' > font_properties
  ```
- 打开 jTessBoxEditor -> Box Editor -> Open，选择 `num.font.exp0.tif`
- 在 Box Editor 中调整识别错误的字以及位置，可使用操作 `insert 添加`、`delete 删除`、`merge 合并`、`split 分离`
- 每个图都调整完成后，点击“save”保存
- 生成训练数据
 ```shell
    # 
    echo Run Tesseract for Training..
    tesseract num.font.exp0.tif num.font.exp0 nobatch box.train
    
    echo Compute the Character Set..
    unicharset_extractor num.font.exp0.box
    mftraining -F font_properties -U unicharset -O num.unicharset num.font.exp0.tr
    
    
    echo Cntraining..
    cntraining num.font.exp0.tr
    
    echo Rename Files..
    mv normproto num.normproto
    mv inttemp num.inttemp
    mv pffmtable num.pffmtable
    mv shapetable num.shapetable  
    
    echo Create Tessdata..
    combine_tessdata num.
  ```
- 处理完成后, 将 num.trainddata 文件拷贝到 data 目录
- 重新识别图片, 验证识别率
  ```shell
  tesseract order.jpg num01 -l num
  ```

## 参考

- https://blog.csdn.net/weixin_42872122/article/details/123730558
- https://xtuz.cmypsc.com/detail-62.html
