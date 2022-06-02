import logo from './logo.svg';
import './App.css';
import SignIn from "./page/sign/SignIn";
import Main from "./page/main/Main";
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {
  return (
    <>
      <BrowserRouter>
          <Routes>
              <Route path={"/main"} element={<Main />} />
              <Route path={"/"} element={<SignIn />} />
          </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
