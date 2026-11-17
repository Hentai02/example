<template>
  <div class="min-h-screen bg-white p-6 rounded-lg shadow-lg">
    <h1 class="text-3xl font-bold mb-6">文件管理</h1>
    <div class="mb-6">
      <ul class="space-y-4">
        <FileItem
          v-for="file in files"
          :key="file.id"
          :file="file"
          :isRoot="true"
          @assignUserGroup="openAssignUserGroupDialog"
          @setPermissions="openSetPermissionsDialog"
        />
      </ul>
    </div>

    <!-- 分配用户组对话框 -->
    <div
      v-if="showAssignUserGroupDialog"
      class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 class="text-2xl font-semibold mb-4">分配用户组</h2>
        <p class="text-gray-700">选择用户组并分配给文件。</p>
        <select
          v-model="selectedUserGroup"
          class="mt-2 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        >
          <option v-for="group in userGroups" :key="group" :value="group">{{ group }}</option>
        </select>
        <div class="flex justify-end mt-4">
          <button
            @click="closeAssignUserGroupDialog"
            class="mr-2 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-gray-200 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
          >
            取消
          </button>
          <button
            @click="confirmAssignUserGroup"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            确认分配
          </button>
        </div>
      </div>
    </div>

    <!-- 设置权限对话框 -->
    <div
      v-if="showSetPermissionsDialog"
      class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75"
    >
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 class="text-2xl font-semibold mb-4">设置权限</h2>
        <p class="text-gray-700">选择权限并设置给文件。</p>
        <select
          v-model="selectedPermission"
          class="mt-2 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-green-500 focus:border-green-500 sm:text-sm"
        >
          <option v-for="permission in permissions" :key="permission" :value="permission">
            {{ permission }}
          </option>
        </select>
        <div class="flex justify-end mt-4">
          <button
            @click="closeSetPermissionsDialog"
            class="mr-2 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-gray-200 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500"
          >
            取消
          </button>
          <button
            @click="confirmSetPermissions"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
          >
            确认设置
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import FileItem from './FileItem.vue'

const files = ref([
  {
    id: '1',
    name: '文件夹1',
    description: '这是文件夹1的描述',
    userGroup: '组A',
    permissions: '读写',
    children: [
      {
        id: '1-1',
        name: '子文件1-1',
        description: '这是子文件1-1的描述',
        userGroup: '组A',
        permissions: '读写',
        children: [],
      },
      {
        id: '1-2',
        name: '子文件1-2',
        description: '这是子文件1-2的描述',
        userGroup: '组B',
        permissions: '只读',
        children: [],
      },
    ],
  },
  {
    id: '2',
    name: '文件夹2',
    description: '这是文件夹2的描述',
    userGroup: '组B',
    permissions: '只读',
    children: [
      {
        id: '2-1',
        name: '子文件2-1',
        description: '这是子文件2-1的描述',
        userGroup: '组C',
        permissions: '读写',
        children: [],
      },
    ],
  },
  {
    id: '3',
    name: '文件夹3',
    description: '这是文件夹3的描述',
    userGroup: '组C',
    permissions: '读写',
    children: [],
  },
])

const userGroups = ref(['组A', '组B', '组C', '组D'])
const permissions = ref(['只读', '读写'])

const selectedFile = ref(null)
const selectedUserGroup = ref('')
const selectedPermission = ref('')

const showAssignUserGroupDialog = ref(false)
const showSetPermissionsDialog = ref(false)

const openAssignUserGroupDialog = (file) => {
  selectedFile.value = file
  selectedUserGroup.value = file.userGroup
  showAssignUserGroupDialog.value = true
}

const closeAssignUserGroupDialog = () => {
  showAssignUserGroupDialog.value = false
}

const confirmAssignUserGroup = () => {
  if (selectedFile.value) {
    selectedFile.value.userGroup = selectedUserGroup.value
    // 更新子文件的用户组
    updateChildrenUserGroup(selectedFile.value.children, selectedUserGroup.value)
    console.log('分配用户组', selectedFile.value)
    closeAssignUserGroupDialog()
  }
}

const updateChildrenUserGroup = (children, userGroup) => {
  if (children && children.length) {
    children.forEach((child) => {
      child.userGroup = userGroup
      updateChildrenUserGroup(child.children, userGroup)
    })
  }
}

const openSetPermissionsDialog = (file) => {
  selectedFile.value = file
  selectedPermission.value = file.permissions
  showSetPermissionsDialog.value = true
}

const closeSetPermissionsDialog = () => {
  showSetPermissionsDialog.value = false
}

const confirmSetPermissions = () => {
  if (selectedFile.value) {
    selectedFile.value.permissions = selectedPermission.value
    // 更新子文件的权限
    updateChildrenPermissions(selectedFile.value.children, selectedPermission.value)
    console.log('设置权限', selectedFile.value)
    closeSetPermissionsDialog()
  }
}

const updateChildrenPermissions = (children, permissions) => {
  if (children && children.length) {
    children.forEach((child) => {
      child.permissions = permissions
      updateChildrenPermissions(child.children, permissions)
    })
  }
}
</script>
