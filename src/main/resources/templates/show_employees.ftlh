<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!-- Tailwind CSS via CDN -->
    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">
    <style>
        /* Стили для модального окна Удаления */
        #deleteEmployeeModal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 9999;
            align-items: center;
            justify-content: center;
        }
        .modal-container {
            background-color: white;
            border-radius: 0rem;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            padding: 1.5rem;
            width: 90%;
            max-width: 400px;
            transform: scale(0.9);
            opacity: 0;
            transition: all 0.2s ease-out;
        }
        #deleteEmployeeModal.show .modal-container {
            transform: scale(1);
            opacity: 1;
        }
        .modal-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #0a3d62;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 0.75rem;
            margin-top: 1.5rem;
        }
        .btn-cancel {
            padding: 0.5rem 1rem;
            background-color: #e2e8f0;
            color: #2d3748;
            border: none;
            border-radius: 0 rem;
            font-size: 0.875rem;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-cancel:hover {
            background-color: #cbd5e0;
        }
        .btn-delete {
            padding: 0.5rem 1rem;
            background-color: #e53e3e;
            color: white;
            border: none;
            border-radius: 0rem;
            font-size: 0.875rem;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-delete:hover {
            background-color: #c53030;
        }
        .warning-icon {
            width: 1.5rem;
            height: 1.5rem;
            color: #dd6b20;
        }
    </style>
</head>

<body class="bg-gray-50 min-h-screen flex">
    <!-- Show employees page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <div class="w-full mx-auto bg-white shadow-xl border border-gray-100">
            <div class="bg-sky-900 px-6 py-1 text-white text-center">
                <h2 class="text-2xl font-semibold">Список сотрудников</h2>
                <p class="text-sky-200">Страница ${currentPage + 1} из ${totalPages}, всего: ${totalElements} записей</p>
            </div>
            <!-- Caption -->
            <div class="p-6 overflow-x-auto">
                <#if employees?has_content> <!-- IF employees?has_content -->
                    <table class="w-full text-sm text-left text-gray-700 table-auto">
                        <thead class="uppercase border-t border-gray-100 bg-gray-50">
                            <!-- First Name -->
                            <th class="px-6 p-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=firstName&direction=${(sortField == 'firstName' && sortDirection == 'asc')?string('desc','asc')}'">
                                Имя
                                <span class="text-gray-800 text-2xl">
                                        <#if sortField == "firstName">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                </span>
                            </th>
                            <!-- Last Name -->
                            <th class="px-6 p-top-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=lastName&direction=${(sortField == 'lastName' && sortDirection == 'asc')?string('desc','asc')}'">Фамилия
                                <span class="text-gray-800 text-2xl">
                                        <#if sortField == "lastName">
                                            <#if sortDirection == "asc">↓</#if>
                                            <#if sortDirection == "desc">↑</#if>
                                        </#if>
                                </span>
                            </th>
                        </thead>
                    </table>
                <#else>
                    <p class="text-center py-6 text-gray-500">Сотрудники не найдены.</p>
                </#if>                 <!-- IF employees?has_content -->
            </div> <!-- Caption -->
        </div> <!-- Header -->
    </div> <!-- Main Content -->
</body>
</html>