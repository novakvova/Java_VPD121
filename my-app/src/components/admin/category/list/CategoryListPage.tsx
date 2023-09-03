import {useEffect, useState} from "react";
import http_common from "../../../../http_common.ts";
import {ICategoryItem} from "./types.ts";
import {Link} from "react-router-dom";
import ModalDelete from "../../../common/ModalDelete.tsx";

const CategoryListPage = () => {
    const [list, setList] = useState<ICategoryItem[]>([]);
    useEffect(() => {
        // console.log("Loaded component");
        http_common.get<ICategoryItem[]>("api/category")
            .then(resp=> {
                //console.log("Server info", resp.data);
                setList(resp.data);
            });
    }, []);

    const deleteCategory = (id: number) => {
        console.log("Delete by id", id);
    }
    const content = list.map(item=> (
        <tr className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600" key={item.id}>
            <td className="w-32 p-4">
                <img src={`${http_common.getUri()}images/150_${item.image}`} alt="Apple Watch"/>
            </td>
            <td className="px-6 py-4 font-semibold text-gray-900 dark:text-white">
                {item.name}
            </td>

            <td className="px-6 py-4 font-semibold text-gray-900 dark:text-white">
                {item.description}
            </td>
            <td className="px-6 py-4">
                <a href="#" className="font-medium text-blue-600 dark:text-blue-500 hover:underline">Змінить</a>
                <ModalDelete id={item.id} text={item.name} deleteFunc={deleteCategory}/>
            </td>
        </tr>
    ));


    return (
        <>

            <div className="mx-auto text-center">
                <h1 className="text-3xl  font-bold text-black sm:text-5xl">Список категорій</h1>
            </div>

            <Link to={"/create"}
                className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 border border-green-700 rounded">
                Додати
            </Link>
            <div className="mt-4 relative overflow-x-auto shadow-md sm:rounded-lg">
                <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                    <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                    <tr>
                        <th scope="col" className="px-6 py-3">
                            <span className="sr-only">Image</span>
                        </th>
                        <th scope="col" className="px-6 py-3">
                            Назва
                        </th>
                        <th scope="col" className="px-6 py-3">
                            Опис
                        </th>
                        <th scope="col" className="px-6 py-3">
                            Дії
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                        {content}
                    </tbody>
                </table>
            </div>

        </>
    );
}

export default CategoryListPage;
