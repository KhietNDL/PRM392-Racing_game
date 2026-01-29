# 📌 VINH'S QUICK REFERENCE CARD

## 🎯 Your 3 Main Classes

### 1️⃣ MusicManager.java (Sound Controller)
```java
// Start background music (loops automatically)
MusicManager.getInstance().startBgm(this, R.raw.congratulation_bgm);

// Play sound effect once
MusicManager.getInstance().playEffect(this, R.raw.win_sound);

// Control music
MusicManager.getInstance().pauseBgm();   // Pause
MusicManager.getInstance().resumeBgm();  // Resume
MusicManager.getInstance().stopBgm();    // Stop
```

### 2️⃣ WinnerActivity.java (Winner Screen)
```java
// Receives from GameActivity:
winnerId, winnerName, winnerImageRes
moneyBefore, moneyAfter, betAmount, betOnId

// Does:
- Shows winner with animation
- Plays congratulation BGM
- Next button → BetResultActivity
- Back button disabled
```

### 3️⃣ BetResultActivity.java (Result Screen)
```java
// Receives from WinnerActivity:
(same data passed through)

// Does:
- Shows win/lose message
- Displays money (white/green/red)
- Play Again → MainActivity
- Home → MainActivity
- Stops BGM on exit
```

---

## 🎨 Color Rules

| Element | Condition | Color | Hex |
|---------|-----------|-------|-----|
| Money Before | Always | White | `#FFFFFF` |
| Money Change | Profit (+) | Green | `#4CAF50` |
| Money Change | Loss (-) | Red | `#F44336` |
| Money Change | None (0) | White | `#FFFFFF` |
| Money After | Higher | Green | `#4CAF50` |
| Money After | Lower | Red | `#F44336` |
| Money After | Same | White | `#FFFFFF` |

---

## 🔄 Screen Flow

```
GameActivity
    ↓ (race ends)
WinnerActivity ← 🎵 START congratulation_bgm
    ↓ (click Next)
    ↓ (finish() - can't go back)
BetResultActivity ← 🎵 BGM continues
    ↓ (click Home or Play Again)
    ↓ (🛑 STOP BGM)
MainActivity
```

---

## 🎵 Audio Files Needed

| File | Location | Purpose | Status |
|------|----------|---------|--------|
| `login_sound.mp3` | `res/raw/` | Login screen BGM | ✅ Exists |
| `congratulation_bgm.mp3` | `res/raw/` | Winner/Result BGM | ⚠️ Replace |
| `win_sound.mp3` | `res/raw/` | Victory effect | ❌ Add |
| `race_sound.mp3` | `res/raw/` | Racing BGM | ❌ Add |

**Get free music:** freesound.org, incompetech.com, zapsplat.com

---

## 🎬 Animations Used

### Scale (Winner Image)
```java
0% → 100% size, 800ms, center pivot
```

### Fade (Winner Title)
```java
0% → 100% opacity, 1000ms, 300ms delay
```

---

## 🔧 Integration Code

### For GameActivity (Đức Anh):
```java
// When race ends, call this:
Intent intent = new Intent(this, WinnerActivity.class);
intent.putExtra("winnerId", winnerId);
intent.putExtra("winnerName", "Horse " + winnerId);
intent.putExtra("winnerImageRes", R.drawable.horse1);
intent.putExtra("moneyBefore", moneyBefore);
intent.putExtra("moneyAfter", moneyAfter);
intent.putExtra("betAmount", betAmount);
intent.putExtra("betOnId", betOnId);
startActivity(intent);
finish();
```

### What BetManager (Tín) Provides:
```java
int moneyBefore = Player.getMoney();
int betAmount = Player.getBetAmount();
int betOnId = Player.getBetOnId();
boolean won = (betOnId == winnerId);
int moneyAfter = BetManager.calculate(moneyBefore, betAmount, won);
```

---

## 🐛 Testing Checklist

- [ ] Build succeeds in Android Studio
- [ ] WinnerActivity shows correctly
- [ ] BetResultActivity shows correctly
- [ ] Music plays and loops
- [ ] Music stops on exit
- [ ] Animations smooth
- [ ] Colors correct
- [ ] Back button disabled
- [ ] Can't return to winner screen

---

## 📱 View IDs Quick Reference

### activity_winner.xml
- `ivWinnerIcon` - Trophy
- `tvWinnerTitle` - Title text
- `ivWinnerImage` - Winner image
- `tvCelebration` - "Champion!"
- `btnNext` - Next button

### activity_bet_result.xml
- `ivResultIcon` - Win/lose icon
- `tvResultTitle` - Result message
- `tvMoneyBefore` - Previous money
- `tvMoneyChange` - +/- amount
- `tvMoneyAfter` - Current money
- `btnPlayAgain` - Play again
- `btnHome` - Home button

---

## 💾 Files You Created

✅ **Java (3 files)**
- `sound/MusicManager.java`
- `ui/WinnerActivity.java`
- `ui/BetResultActivity.java`

✅ **XML (5 files)**
- `layout/activity_winner.xml`
- `layout/activity_bet_result.xml`
- `drawable/ic_trophy.xml`
- `drawable/ic_win.xml`
- `drawable/ic_lose.xml`

✅ **Updated (2 files)**
- `values/colors.xml`
- `AndroidManifest.xml`

✅ **Docs (4 files)**
- `VINH_PART_README.md`
- `VINH_QUICK_START.md`
- `VINH_FLOW_LOGIC.md`
- `VINH_SUMMARY.md`

**Total: 14 files** 🎉

---

## 🎓 Key Concepts to Explain

1. **Singleton Pattern** - One MusicManager instance
2. **MediaPlayer vs SoundPool** - Long vs short audio
3. **Intent Extras** - Passing data between activities
4. **Lifecycle Methods** - onCreate, onPause, onResume, onDestroy
5. **View Animations** - ScaleAnimation, AlphaAnimation
6. **Color Coding** - Conditional text colors
7. **Navigation Control** - Disabling back button
8. **Resource Management** - Releasing MediaPlayer

---

## ⚡ Quick Commands

### Build Project
```bash
cd PRM392-Racing_game
./gradlew assembleDebug
```

### Open in Android Studio
```
File → Open → PRM392-Racing_game
```

### Run on Device
```
Shift + F10
```

---

## 📞 Need Help?

1. Read `VINH_PART_README.md` - full technical details
2. Read `VINH_QUICK_START.md` - setup steps
3. Read `VINH_FLOW_LOGIC.md` - flow diagrams
4. Read `VINH_SUMMARY.md` - what you accomplished
5. Ask team members for integration data

---

**PRINT THIS PAGE - PIN TO WALL - REFERENCE ANYTIME** 📌

**Status: ✅ Complete**
**Your Part: Sound & Result Screen**
**Lines of Code: 624**
**Files Created: 14**
**Ready for: Integration**
