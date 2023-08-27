import './App.css'
import {Routes, Route} from 'react-router-dom';
import DefaultLayout from "./components/containers/default/DefaultLayout.tsx";
import CategoryListPage from "./components/admin/category/list/CategoryListPage.tsx";
import CategoryCreatePage from "./components/admin/category/create/CategoryCreatePage.tsx";
function App() {

  return (
    <>
        <Routes>
            <Route path={"/"} element={<DefaultLayout/>}>
                <Route index element = {<CategoryListPage/>} />
                <Route path={"create"} element = {<CategoryCreatePage/>} />
            </Route>
        </Routes>
    </>
  )
}

export default App
