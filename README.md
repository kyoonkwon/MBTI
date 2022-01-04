# MBTI.

**M**anagement of **B**ooks, **T**ime schedules & **I**nteraction.

## Made By.

[고려대학교 컴퓨터학과 20학번 노정훈](https://www.github.com/overthestream)

[KAIST 전산학부 17학번 권기훈](https://www.github.com/kyoonkwon)

## Summary.

라이프 스타일을 관리하기 위한 3가지 탭이 있는 애플리케이션.

### Books

#### First,

![books_home](./src/books_home.png)

서재 탭의 첫 화면입니다.

개발자의 pick, 20가지 책 글귀가 초기 이미지로 수록되어 있습니다.

서재의 썸네일 이미지들은 원본 이미지에서 중심 부분을 1:1 비율로 보여줍니다.

![핀치 줌]()
두 손가락으로 확대 및 축소도 가능합니다.

#### Load Image

새로운 글을 가져오려면, 첫번째 카메라 이미지를 클릭합니다.
![카메라 버튼 클릭]()

##### Load From Camera

![카메라로 찍기]()
카메라로 찍어서 글귀를 가져올 수 있습니다.

##### Load From Gallery

![갤러리에서 가져오기]()
이미 갤러리에 존재하는 글을 새로 가져올 수도 있습니다.

#### Get Text

![Thumbnail Click]()

각각의 글을 클릭하면, 크게 글을 볼 수 있는 창으로 갑니다.

![get OCR text]()

버튼을 클릭하여 글귀를 가져옵니다.

![get null]()

사진에서 글귀가 검색되지 않을 수도 있습니다.

![memo]()

글귀를 보고 생각한 내용을 따로 메모해둘 수도 있습니다.

![delete]()
삭제 버튼을 누르면 글을 삭제하고,

![confirm]()
확인 버튼을 눌러 원래 화면으로 돌아갑니다.

책을 읽을 때 기억해두고 싶은 내용들을 저장하고, 메모해두세요.
기록을 통해 기억력의 한계를 극복할 수 있습니다.

### Time schedules

### Interaction

## Implementation.

android studio의 bottom navigation activity 템플릿을 이용하여,
MainActivity 상에서 Fragment 전환만으로 기능하는 애플리케이션입니다.

### Books

### Time schedules

### Interaction


## How To Use.

Github Actions를 이용하여 main branch에 commit, pull request시 자동으로 build 후 apk파일을 생성합니다.

![github_actions](./src/github_actions.png)

- actions 탭으로 들어가 주세요.

![workflow](./src/workflow.png)

- workflow 중 최근의 커밋 메시지 명을 클릭해서 들어가 주세요.

![apk](./src/apk.png)

- Artifacts 탭의 app을 클릭 시 apk.zip 파일을 다운로드 받으실 수 있습니다.
  