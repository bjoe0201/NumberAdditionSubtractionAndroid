# 變更紀錄

## v1.0.2 (2026-05-05)
- GitHub Releases 改為發佈 `assembleRelease` 產出的 signed release APK
- 新增私密 release keystore 簽章設定，支援 `keystore.properties` 或環境變數
- `assembleRelease` 在缺少 release 簽章資訊時會直接失敗，避免誤發 unsigned APK
- 更新發佈文件與 APK asset 命名規則為 `NumberAdditionSubtractionAndroid-vX.Y.Z-release.apk`

## v1.0.1 (2026-05-05)
- 修正測試畫面、設定畫面、語言畫面、結果畫面等非主畫面在直式/橫式切換時會被重設回主畫面的問題
- 保留測試中目前題目進度，旋轉螢幕後不會重新開始或跳回首頁
- 保留結果畫面的表情名字輸入與分數儲存狀態
- 旋轉後重新綁定遊戲語音回饋 callback，避免指向已銷毀的 Activity

## v1.0.0 (2026-05-05)
- 初版發佈，基於 CountNumberZoo_Android 移植
- 加法模式：點擊動物數加總
- 減法模式：畫 X 去除動物求差
- 混合模式：每題隨機加法或減法
- 三語支援：中文、英文、日文
- TTS 語音回饋
- 排行榜（使用表情符號命名）

