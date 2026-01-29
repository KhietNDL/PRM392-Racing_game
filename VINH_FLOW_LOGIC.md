# 🎯 VINH'S PART - SCREEN FLOW & LOGIC

## 📱 Screen Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         GAME FLOW                                │
└─────────────────────────────────────────────────────────────────┘

    LoginActivity (Khiết)
           ↓
    MainActivity / BettingActivity (Tín)
           ↓ [user places bet]
    GameActivity (Đức Anh)
           ↓ [race starts]
    🎵 Play race_sound.mp3
           ↓ [race finishes]
    🛑 Stop race_sound.mp3
           ↓
    ╔═══════════════════════════════════════╗
    ║    WinnerActivity (Vinh - Screen 1)   ║
    ╠═══════════════════════════════════════╣
    ║  🏆 Trophy Icon                       ║
    ║  🎉 Winner: Horse 1 🎉                ║
    ║  🐎 [Winner Image/GIF]                ║
    ║  🏆 Champion! 🏆                      ║
    ║                                       ║
    ║        [Next ➡ Button]                ║
    ╚═══════════════════════════════════════╝
           ↓ 🎵 congratulation_bgm.mp3 PLAYING
           ↓ [user clicks Next]
           ↓ ⚠️ CANNOT GO BACK
           ↓
    ╔═══════════════════════════════════════╗
    ║  BetResultActivity (Vinh - Screen 2)  ║
    ╠═══════════════════════════════════════╣
    ║  ✅ or ❌ Result Icon                 ║
    ║  🎊 Congratulations! You Won! 🎊      ║
    ║  ┌───────────────────────────────┐   ║
    ║  │  Previous: $1000  (white)     │   ║
    ║  │       +500        (green)     │   ║
    ║  │  ─────────────────            │   ║
    ║  │  Current: $1500   (green)     │   ║
    ║  └───────────────────────────────┘   ║
    ║                                       ║
    ║     [🎮 Play Again Button]            ║
    ║     [🏠 Home Button]                  ║
    ╚═══════════════════════════════════════╝
           ↓ 🎵 congratulation_bgm.mp3 STILL PLAYING
           ↓ ⚠️ CANNOT GO BACK
           ↓
     [Play Again] → MainActivity (🛑 stop BGM)
     [Home] → MainActivity (🛑 stop BGM)
```

---

## 🎵 Sound Logic

### Sound Flow:
```
App Start
    ↓
LoginActivity: 🎵 login_sound.mp3 (looped)
    ↓
MainActivity: 🛑 stop login_sound
    ↓
GameActivity (race starts): 🎵 race_sound.mp3 (looped)
    ↓
GameActivity (race ends): 🛑 stop race_sound
    ↓
WinnerActivity: 🎵 congratulation_bgm.mp3 (looped)
    ↓
BetResultActivity: 🎵 congratulation_bgm continues
    ↓
Click Home/Play Again: 🛑 stop congratulation_bgm
```

### MusicManager Methods:
```java
// Start background music (auto-loops)
MusicManager.getInstance().startBgm(context, R.raw.congratulation_bgm);

// Play sound effect (plays once)
MusicManager.getInstance().playEffect(context, R.raw.win_sound);

// Control playback
MusicManager.getInstance().pauseBgm();
MusicManager.getInstance().resumeBgm();
MusicManager.getInstance().stopBgm();

// Cleanup
MusicManager.getInstance().release();
```

---

## 💰 Money Display Logic

### Example Scenarios:

#### Scenario 1: Player WINS
```
Before: $1000 (white)
Bet: $200 on Horse 1
Winner: Horse 1 ✅
Payout: $400 (2x bet)
Change: +$400 (green)
After: $1400 (green - higher than before)
```

#### Scenario 2: Player LOSES
```
Before: $1000 (white)
Bet: $200 on Horse 2
Winner: Horse 1 ❌
Payout: $0
Change: -$200 (red)
After: $800 (red - lower than before)
```

#### Scenario 3: No Bet
```
Before: $1000 (white)
Bet: $0
Winner: Horse 1
Change: ±0 (white)
After: $1000 (white - same as before)
```

### Color Rules:
```java
// Money Before: ALWAYS WHITE
tvMoneyBefore.setTextColor(ContextCompat.getColor(this, R.color.white));

// Money Change:
if (change > 0) {
    tvMoneyChange.setText("+" + change);
    tvMoneyChange.setTextColor(R.color.win_green); // GREEN
} else if (change < 0) {
    tvMoneyChange.setText(String.valueOf(change)); // Shows "-200"
    tvMoneyChange.setTextColor(R.color.lose_red); // RED
} else {
    tvMoneyChange.setText("±0");
    tvMoneyChange.setTextColor(R.color.white); // WHITE
}

// Money After:
if (moneyAfter > moneyBefore) {
    tvMoneyAfter.setTextColor(R.color.win_green); // GREEN (profit)
} else if (moneyAfter < moneyBefore) {
    tvMoneyAfter.setTextColor(R.color.lose_red); // RED (loss)
} else {
    tvMoneyAfter.setTextColor(R.color.white); // WHITE (no change)
}
```

---

## 🎬 Animation Logic

### WinnerActivity Animations:

#### 1. Winner Image - Scale Animation
```java
// Image grows from 0% to 100% size
ScaleAnimation scaleAnimation = new ScaleAnimation(
    0.0f, 1.0f,  // X: 0% → 100%
    0.0f, 1.0f,  // Y: 0% → 100%
    Animation.RELATIVE_TO_SELF, 0.5f,  // pivot X center
    Animation.RELATIVE_TO_SELF, 0.5f   // pivot Y center
);
scaleAnimation.setDuration(800);  // 800ms
scaleAnimation.setFillAfter(true); // keep at 100% after
ivWinnerImage.startAnimation(scaleAnimation);
```

#### 2. Title Text - Fade In Animation
```java
// Text fades from invisible to visible
AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
fadeIn.setDuration(1000);  // 1 second
fadeIn.setStartOffset(300); // wait 300ms before starting
tvWinnerTitle.startAnimation(fadeIn);
```

### Animation Timeline:
```
0ms     ────────────────────────────────────
        ↓ Image starts scaling from 0%
300ms   ────────────────────────────────────
        ↓ Title starts fading in
800ms   ────────────────────────────────────
        ↓ Image reaches 100%
1300ms  ────────────────────────────────────
        ↓ Title reaches 100% opacity
        ✅ All animations complete
```

---

## 🔒 Back Button Behavior

### Problem:
By default, Android back button would let user go back from BetResultActivity → WinnerActivity → GameActivity. This breaks our flow!

### Solution:
Override `onBackPressed()` to do nothing:

```java
@Override
public void onBackPressed() {
    // Do nothing - user must click Next/Home/Play Again
    // Don't call super.onBackPressed()
}
```

### User Navigation Rules:
- ✅ WinnerActivity: MUST click "Next" → no going back
- ✅ BetResultActivity: MUST click "Home" or "Play Again" → no going back
- ❌ Back button: Does nothing on both screens
- ❌ WinnerActivity: Cannot be revisited after clicking "Next"

---

## 📦 Intent Data Structure

### From GameActivity → WinnerActivity:
```java
Intent intent = new Intent(GameActivity.this, WinnerActivity.class);

// Winner info
intent.putExtra("winnerId", 1);           // int: which horse/car won
intent.putExtra("winnerName", "Horse 1"); // String: display name
intent.putExtra("winnerImageRes", R.drawable.horse1); // int: drawable ID

// Money info
intent.putExtra("moneyBefore", 1000);     // int: money before race
intent.putExtra("moneyAfter", 1400);      // int: money after race

// Bet info
intent.putExtra("betAmount", 200);        // int: how much bet
intent.putExtra("betOnId", 1);            // int: which horse/car user bet on

startActivity(intent);
finish(); // Remove GameActivity from stack
```

### From WinnerActivity → BetResultActivity:
```java
Intent intent = new Intent(WinnerActivity.this, BetResultActivity.class);

// Pass all data forward
intent.putExtra("winnerId", winnerId);
intent.putExtra("winnerName", winnerName);
intent.putExtra("moneyBefore", moneyBefore);
intent.putExtra("moneyAfter", moneyAfter);
intent.putExtra("betAmount", betAmount);
intent.putExtra("betOnId", betOnId);

startActivity(intent);
finish(); // Remove WinnerActivity from stack (cannot go back)
```

---

## 🎨 UI Component IDs

### activity_winner.xml:
```xml
R.id.ivWinnerIcon    → Trophy ImageView
R.id.tvWinnerTitle   → "Winner: Horse 1" TextView
R.id.ivWinnerImage   → Horse/Car ImageView
R.id.tvCelebration   → "Champion!" TextView
R.id.btnNext         → "Next" Button
```

### activity_bet_result.xml:
```xml
R.id.ivResultIcon     → Win/Lose ImageView
R.id.tvResultTitle    → "Congratulations!" TextView
R.id.cardBetResult    → Money display CardView
R.id.tvMoneyBefore    → "Previous: $1000" TextView
R.id.tvMoneyChange    → "+500" TextView
R.id.tvMoneyAfter     → "Current: $1500" TextView
R.id.btnPlayAgain     → "Play Again" Button
R.id.btnHome          → "Home" Button
```

---

## 🎨 Colors Used

```xml
<!-- values/colors.xml -->
<color name="white">#FFFFFFFF</color>        <!-- Money before -->
<color name="win_green">#4CAF50</color>      <!-- Profit, Win message -->
<color name="lose_red">#F44336</color>       <!-- Loss, Lose message -->
<color name="bg_dark">#1E1E1E</color>        <!-- Background -->
<color name="gold">#FFD700</color>           <!-- Trophy, buttons -->
```

---

## 🔄 Activity Lifecycle & Sound

### WinnerActivity Lifecycle:
```java
onCreate():
    - Initialize views
    - Get intent data
    - Display winner info
    - START congratulation_bgm ← 🎵
    - Start animations

onPause():
    - PAUSE congratulation_bgm ← ⏸️

onResume():
    - RESUME congratulation_bgm ← ▶️

onDestroy():
    - DON'T stop BGM (it continues to BetResultActivity)
```

### BetResultActivity Lifecycle:
```java
onCreate():
    - Initialize views
    - Get intent data
    - Calculate win/loss
    - Display money
    - (BGM still playing from WinnerActivity) ← 🎵

onPause():
    - DON'T pause (BGM keeps playing)

onResume():
    - DON'T resume (BGM already playing)

onDestroy():
    - DON'T stop here (button handlers stop it)

btnHome.onClick():
    - STOP congratulation_bgm ← 🛑
    - Go to MainActivity

btnPlayAgain.onClick():
    - STOP congratulation_bgm ← 🛑
    - Go to MainActivity
```

---

## ✅ Testing Checklist

### Sound Tests:
- [ ] BGM starts when WinnerActivity opens
- [ ] BGM loops continuously
- [ ] BGM continues when moving to BetResultActivity
- [ ] BGM stops when clicking Home
- [ ] BGM stops when clicking Play Again
- [ ] BGM pauses when phone call comes in
- [ ] BGM resumes after phone call
- [ ] No crashes when rotating screen

### UI Tests:
- [ ] Trophy icon displays
- [ ] Winner name displays correctly
- [ ] Winner image displays
- [ ] Animations play smoothly
- [ ] Money before shows white
- [ ] Money change shows green when positive
- [ ] Money change shows red when negative
- [ ] Money after shows correct color
- [ ] All buttons work
- [ ] Back button does nothing

### Flow Tests:
- [ ] Can't skip WinnerActivity
- [ ] Can't go back from BetResultActivity
- [ ] Can't return to WinnerActivity after Next
- [ ] Home button goes to correct activity
- [ ] Play Again button goes to correct activity

---

**This document explains all logic in your part!**
